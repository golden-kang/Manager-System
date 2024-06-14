package com.golden.ms.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UpdateUserRequest implements Serializable {
    private String accountName;
    private List<String> endpoints; // use endpoints instead of endpoint
}
