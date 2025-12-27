package com.cigi.pickthem.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cigi.pickthem.exception.BadRequestException;
import com.cigi.pickthem.exception.NotFoundException;
import com.cigi.pickthem.exception.UnauthorizedException;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response) {

        AuthenticationResponse authResponse = service.register(request);

        // Générer le refresh token
        var user = service.getUserByEmail(request.getEmail());
        String refreshToken = service.generateRefreshToken(user);

        // Stocker le refresh token dans un cookie HttpOnly
        ResponseCookie refreshCookie = buildRefreshTokenCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // Retourner seulement l'access token dans le body
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response) {

        AuthenticationResponse authResponse = service.authenticate(request);

        // Générer le refresh token
        var user = service.getUserByEmail(request.getEmail());
        String refreshToken = service.generateRefreshToken(user);

        // Stocker le refresh token dans un cookie HttpOnly
        ResponseCookie refreshCookie = buildRefreshTokenCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // Retourner seulement l'access token dans le body
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        try {
            String refreshToken = extractRefreshTokenFromCookie(request);
            if (refreshToken == null || refreshToken.isEmpty()) {
                throw new UnauthorizedException("Refresh token missing");
            }

            AuthenticationResponse authResponse = service.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(authResponse);

        } catch (NotFoundException | UnauthorizedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadRequestException("refresh token invalid");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        // Supprimer le cookie refresh token
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false) // true en prod
                .path("/")
                .maxAge(0) // delete cookie
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok()
                .body("Logged out successfully");
    }

    private ResponseCookie buildRefreshTokenCookie(String token) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true) // Protection XSS
                .secure(false) // true en prod (HTTPS)
                .sameSite("lax") // Protection CSRF
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 jours
                .build();
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}