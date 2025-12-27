package com.cigi.pickthem.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // 🔹 API exceptions (Conflict, NotFound, Unauthorized, etc.)
        @ExceptionHandler(ApiException.class)
        public ResponseEntity<ApiError> handleApiException(
                        ApiException ex,
                        HttpServletRequest request) {

                ApiError error = ApiError.builder()
                                .name(ex.getStatus().name())
                                .message(ex.getMessage())
                                .status(ex.getStatus().value())
                                .statusText(ex.getStatus().getReasonPhrase())
                                .cause(ex.getCause() != null ? ex.getCause().getMessage() : null)
                                .build();

                return ResponseEntity
                                .status(ex.getStatus())
                                .body(error);
        }

        @ExceptionHandler(JwtAuthenticationException.class)
        public ResponseEntity<ApiError> handleJwtException(JwtAuthenticationException ex) {
                ApiError error = ApiError.builder()
                                .name(ex.getStatus().name())
                                .message(ex.getMessage())
                                .status(ex.getStatus().value())
                                .statusText(ex.getStatus().getReasonPhrase())
                                .build();
                return ResponseEntity.status(ex.getStatus()).body(error);
        }

}
