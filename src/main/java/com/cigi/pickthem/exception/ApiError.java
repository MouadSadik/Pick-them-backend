package com.cigi.pickthem.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {

    private String name; // e.g. "Unauthorized"
    private String message; // readable message
    private int status; // HTTP status code
    private String statusText; // e.g. "UNAUTHORIZED"
    private String cause; // optional
    private Object data; // optional (extra info)
}
