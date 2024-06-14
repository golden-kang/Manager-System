package com.golden.ms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor()
@Getter
@Setter
public class User implements Serializable {
    private int userId;
    private String accountName;
    private String role;
    private List<String> endpoints;
}
