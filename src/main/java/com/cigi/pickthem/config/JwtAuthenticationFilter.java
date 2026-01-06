package com.cigi.pickthem.config;

import com.cigi.pickthem.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);

            // Vérifier si c'est un refresh token
            if (jwtService.isRefreshToken(jwt)) {
                sendErrorResponse(response, HttpStatus.FORBIDDEN,
                        "Forbidden",
                        "Refresh token cannot be used to access protected resources");
                return;
            }

            final String userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (!jwtService.isValidToken(jwt, userDetails)) {
                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED,
                            "Unauthorized",
                            "Invalid or expired access token");
                    return;
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    "Access token has expired");
        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    "Malformed JWT token");
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    "Invalid JWT signature");
        } catch (Exception ex) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    "JWT authentication failed: " + ex.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response,
            HttpStatus status,
            String name,
            String message) throws IOException {
        ApiError apiError = ApiError.builder()
                .name(name)
                .message(message)
                .status(status.value())
                .statusText(status.name())
                .build();

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
        response.getWriter().flush();
    }

}