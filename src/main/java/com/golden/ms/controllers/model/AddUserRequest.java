package com.golden.ms.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class AddUserRequest implements Serializable {
    private int userId;
    private String accountName;
    private List<String> endpoints; // Rename endpoint to endpoints
}
