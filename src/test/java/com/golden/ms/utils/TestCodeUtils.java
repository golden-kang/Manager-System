package com.golden.ms.utils;

import com.golden.ms.controllers.model.CheckResourceAccessResponse;
import com.golden.ms.exceptions.model.ErrorResponse;
import com.golden.ms.model.ResponseBodyAddUser;
import com.golden.ms.model.ResponseBodyCheckResourceAccessResponse;
import com.google.gson.Gson;

public class TestCodeUtils {
    private static Gson gson = new Gson();

    // used for test
    public static <T> String serializeObject(T object) {
        return gson.toJson(object);
    }

    public static ResponseBodyAddUser deserializeAddUserResponse(String userStr) {
        return gson.fromJson(userStr, ResponseBodyAddUser.class);
    }

    public static ErrorResponse deserializeErrorResponse(String errorResponseStr) {
        return gson.fromJson(errorResponseStr, ErrorResponse.class);
    }

    public static ResponseBodyCheckResourceAccessResponse deserializeCheckResourceAccessResponse(String CheckResourceAccessStr) {
        return gson.fromJson(CheckResourceAccessStr, ResponseBodyCheckResourceAccessResponse.class);
    }

}
