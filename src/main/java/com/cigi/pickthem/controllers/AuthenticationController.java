package com.cigi.pickthem.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cigi.pickthem.domain.dtos.auth.LoginRequest;
import com.cigi.pickthem.domain.dtos.auth.LoginResponse;
import com.cigi.pickthem.domain.dtos.auth.LogoutResponse;
import com.cigi.pickthem.domain.dtos.auth.RefreshResponse;
import com.cigi.pickthem.domain.dtos.auth.RegisterRequest;
import com.cigi.pickthem.domain.dtos.auth.RegisterResponse;
import com.cigi.pickthem.services.AuthenticationService;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        LoginResponse loginResponse = service.login(request);

        // Générer le refresh token
        var user = service.getUserByEmail(request.getEmail());
        String refreshToken = service.generateRefreshToken(user);

        // Stocker le refresh token dans un cookie HttpOnly
        ResponseCookie refreshCookie = buildRefreshTokenCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {
        // Extraire l'ancien refresh token depuis le cookie
        String oldRefreshToken = extractRefreshTokenFromCookie(request);
        // Appel service → renvoie nouveau access token
        RefreshResponse refreshResponse = service.refresh(oldRefreshToken);

        // Générer un nouveau refresh token dans le controller
        var user = service.getUserByEmail(service.getEmailFromRefreshToken(oldRefreshToken));
        String newRefreshToken = service.generateRefreshToken(user);

        ResponseCookie refreshCookie = buildRefreshTokenCookie(newRefreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // Retourner le body (RefreshResponse) sans refresh token
        return ResponseEntity.ok(refreshResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletResponse response) {

        // Supprimer le cookie refresh token
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false) // true en prod
                .path("/")
                .maxAge(0) // delete cookie
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        LogoutResponse logoutResponse = LogoutResponse.builder()
                .message("User logged out successfully")
                .build();

        return ResponseEntity.ok(logoutResponse);
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