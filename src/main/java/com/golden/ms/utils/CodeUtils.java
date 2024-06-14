package com.golden.ms.utils;

import com.golden.ms.exceptions.model.ErrorResponse;
import com.golden.ms.exceptions.model.SuccessResponseBody;
import com.golden.ms.filters.model.UserRole;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;

import java.util.Base64;

public class CodeUtils {
    private static Gson gson = new Gson();
    public static String decodeBase64(String input){
        return new String(Base64.getDecoder().decode(input));
    }

    public static UserRole deserializeUserRole(String userRoleStr) {
        UserRole userRole = gson.fromJson(userRoleStr, UserRole.class);
        return userRole;
    }

    public static String serializeErrorResponse(HttpStatus status, String message) {
        return gson.toJson(new ErrorResponse(status.value(), message));
    }

    public static <T> String serializeSuccessResponse(HttpStatus status, T body) {
        return gson.toJson(new SuccessResponseBody<T>(body));
    }
}
