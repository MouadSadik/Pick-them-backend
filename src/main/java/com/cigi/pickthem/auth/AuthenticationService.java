package com.cigi.pickthem.auth;

import com.cigi.pickthem.config.JwtService;
import com.cigi.pickthem.domain.entities.UserEntity;
import com.cigi.pickthem.exception.BadRequestException;
import com.cigi.pickthem.exception.ConflictException;
import com.cigi.pickthem.exception.NotFoundException;
import com.cigi.pickthem.exception.UnauthorizedException;
import com.cigi.pickthem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {

                if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                        throw new ConflictException("Email already exists");
                }

                var user = UserEntity.builder()
                                .firstname(request.getFirstname())
                                .lastname(request.getLastname())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(request.getRole())
                                .build();
                userRepository.save(user);

                return generateAuthResponse(user);
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                try {
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getEmail(),
                                                        request.getPassword()));
                } catch (org.springframework.security.authentication.BadCredentialsException ex) {
                        // Transformer l'erreur en 400 BadRequest
                        throw new BadRequestException("Invalid credentials");
                }

                var user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new NotFoundException("User not found"));

                return generateAuthResponse(user);
        }

        public AuthenticationResponse refreshAccessToken(String refreshToken) {
                String userEmail = jwtService.extractUsername(refreshToken);

                var user = userRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new NotFoundException("User not found"));

                if (!jwtService.isValidToken(refreshToken, user)) {
                        throw new UnauthorizedException("Invalid or expired refresh token"); // 401
                }

                if (!jwtService.isRefreshToken(refreshToken)) {
                        throw new UnauthorizedException("Token is not a refresh token"); // <-- CHANGÉ 403 → 401
                }

                // Générer un nouveau access token
                String newAccessToken = jwtService.generateAccessToken(user);

                return AuthenticationResponse.builder()
                                .accessToken(newAccessToken)
                                .tokenType("Bearer")
                                .expiresIn(jwtService.getAccessTokenValidity())
                                .build();
        }

        private AuthenticationResponse generateAuthResponse(UserEntity user) {
                String accessToken = jwtService.generateAccessToken(user);

                return AuthenticationResponse.builder()
                                .accessToken(accessToken)
                                .tokenType("Bearer")
                                .expiresIn(jwtService.getAccessTokenValidity())
                                .build();
        }

        public String generateRefreshToken(UserEntity user) {
                return jwtService.generateRefreshToken(user);
        }

        public UserEntity getUserByEmail(String email) {
                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("User not found"));
        }
}