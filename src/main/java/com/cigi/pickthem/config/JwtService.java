package com.cigi.pickthem.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cigi.pickthem.domain.entities.UserEntity;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final static String SECRET_KEY = "2c5192798c3bd8f26d7d72b90548ea3654f416e4b2dcb01c376f6dafb2c7cb79";

    // Durées en millisecondes
    private final static long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 minutes
    private final static long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 jours

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Générer Access Token (courte durée)
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, ACCESS_TOKEN_VALIDITY);
    }

    // Générer Refresh Token (longue durée)
    public String generateRefreshToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("type", "refresh") // <-- obligatoire
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long validity) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isExpiredToken(token);
    }

    public boolean isExpiredToken(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Vérifier si c'est un refresh token
    public boolean isRefreshToken(String token) {
        try {
            return extractAllClaims(token).get("type", String.class).equals("refresh");
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Obtenir la durée de validité en secondes
    public long getAccessTokenValidity() {
        return ACCESS_TOKEN_VALIDITY / 1000;
    }

    public long getRefreshTokenValidity() {
        return REFRESH_TOKEN_VALIDITY / 1000;
    }
}