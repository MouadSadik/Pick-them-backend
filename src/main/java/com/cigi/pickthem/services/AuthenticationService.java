package com.cigi.pickthem.services;

import com.cigi.pickthem.config.JwtService;
import com.cigi.pickthem.domain.dtos.auth.LoginRequest;
import com.cigi.pickthem.domain.dtos.auth.LoginResponse;
import com.cigi.pickthem.domain.dtos.auth.RefreshResponse;
import com.cigi.pickthem.domain.dtos.auth.RegisterRequest;
import com.cigi.pickthem.domain.dtos.auth.RegisterResponse;
import com.cigi.pickthem.domain.entities.UserEntity;
import com.cigi.pickthem.exception.BadRequestException;
import com.cigi.pickthem.exception.ConflictException;
import com.cigi.pickthem.exception.NotFoundException;
import com.cigi.pickthem.exception.UnauthorizedException;
import com.cigi.pickthem.mappers.AuthUserMapper;
import com.cigi.pickthem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final AuthUserMapper authMapper;

        public RegisterResponse register(RegisterRequest request) {

                if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                        throw new ConflictException("Email already exists");
                }

                var user = UserEntity.builder()
                                .username(request.getUsername())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(request.getRole())
                                .build();

                userRepository.save(user);

                return RegisterResponse.builder()
                                .message("User registered successfully")
                                .build();
        }

        public LoginResponse login(LoginRequest request) {

                try {
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getEmail(),
                                                        request.getPassword()));
                } catch (org.springframework.security.authentication.BadCredentialsException ex) {
                        throw new BadRequestException("Invalid credentials");
                }

                var user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new NotFoundException("User not found"));

                String accessToken = jwtService.generateAccessToken(user);

                return LoginResponse.builder()
                                .message("Login successful")
                                .accessToken(accessToken)
                                .user(authMapper.toResponse(user)) // juste appeler le mapper
                                .build();
        }

        public RefreshResponse refresh(String oldRefreshToken) {
                if (oldRefreshToken == null || oldRefreshToken.isEmpty()) {
                        throw new UnauthorizedException("Refresh token missing");
                }

                // Vérifier et récupérer l'utilisateur
                var userEmail = getEmailFromRefreshToken(oldRefreshToken);
                var user = getUserByEmail(userEmail);

                if (!jwtService.isValidToken(oldRefreshToken, user)) {
                        throw new UnauthorizedException("Invalid or expired refresh token");
                }

                if (!jwtService.isRefreshToken(oldRefreshToken)) {
                        throw new UnauthorizedException("Token is not a refresh token");
                }

                // Générer un nouveau access token
                String newAccessToken = jwtService.generateAccessToken(user);

                // Retourner la réponse sans refresh token
                return RefreshResponse.builder()
                                .message("Access token refreshed")
                                .accessToken(newAccessToken)
                                .user(authMapper.toResponse(user)) // directement l'appel
                                .build();
        }

        public String generateRefreshToken(UserEntity user) {
                return jwtService.generateRefreshToken(user);
        }

        public UserEntity getUserByEmail(String email) {
                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("User not found"));
        }

        public String getEmailFromRefreshToken(String refreshToken) {
                return jwtService.extractUsername(refreshToken);
        }

}