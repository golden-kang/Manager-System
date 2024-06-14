package com.golden.ms.filters.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor()
@Getter
@Setter
public class UserRole  implements Serializable {
    private int userId;
    private String accountName;
    private String Role;
}
