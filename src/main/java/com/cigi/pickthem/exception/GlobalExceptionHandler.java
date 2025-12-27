package com.cigi.pickthem.exception;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler({ MalformedJwtException.class, SignatureException.class, UnsupportedJwtException.class })
        public ResponseEntity<ApiError> handleInvalidJwt(Exception ex) {
                ApiError error = ApiError.builder()
                                .name("Unauthorized")
                                .message("Invalid JWT token")
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .statusText(HttpStatus.UNAUTHORIZED.name())
                                .cause(ex.getMessage())
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidationException(
                        MethodArgumentNotValidException ex) {
                Map<String, String> fieldErrors = new HashMap<>();

                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

                ApiError apiError = ApiError.builder()
                                .name("Unprocessable Entity")
                                .message("Validation failed")
                                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                                .statusText(HttpStatus.UNPROCESSABLE_ENTITY.name())
                                .cause(ex.getClass().getSimpleName())
                                .data(fieldErrors)
                                .build();

                return ResponseEntity
                                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                .body(apiError);
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
                ApiError error = ApiError.builder()
                                .name("Bad Request")
                                .message(ex.getMessage())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .statusText(HttpStatus.BAD_REQUEST.name())
                                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException ex) {
                ApiError error = ApiError.builder()
                                .name("Unauthorized")
                                .message(ex.getMessage())
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .statusText(HttpStatus.UNAUTHORIZED.name())
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
                ApiError error = ApiError.builder()
                                .name("Not Found")
                                .message(ex.getMessage())
                                .status(HttpStatus.NOT_FOUND.value())
                                .statusText(HttpStatus.NOT_FOUND.name())
                                .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<ApiError> handleConflict(ConflictException ex) {
                ApiError error = ApiError.builder()
                                .name("Conflict")
                                .message(ex.getMessage())
                                .status(HttpStatus.CONFLICT.value())
                                .statusText(HttpStatus.CONFLICT.name())
                                .build();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ApiError> handleNoResourceFound(NoResourceFoundException ex) {
                ApiError error = ApiError.builder()
                                .name("Not Found")
                                .message("The requested resource does not exist: " + ex.getResourcePath())
                                .status(HttpStatus.NOT_FOUND.value())
                                .statusText(HttpStatus.NOT_FOUND.name())
                                .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiError> handleInvalidJson(HttpMessageNotReadableException ex) {
                ApiError error = ApiError.builder()
                                .name("Bad Request")
                                .message("Malformed JSON request")
                                .status(HttpStatus.BAD_REQUEST.value())
                                .statusText(HttpStatus.BAD_REQUEST.name())
                                .cause(ex.getMostSpecificCause().getMessage())
                                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleGenericException(Exception ex) {
                ApiError error = ApiError.builder()
                                .name("Internal Server Error")
                                .message("An unexpected error occurred")
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .statusText(HttpStatus.INTERNAL_SERVER_ERROR.name())
                                .cause(ex.getMessage())
                                .build();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

}