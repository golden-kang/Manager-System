package com.golden.ms.exceptions.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor()
@Getter
public class ErrorResponse {
    private int statusCode;
    private String message;
}
