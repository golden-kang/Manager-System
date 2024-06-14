package com.golden.ms.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class CheckResourceAccessResponse implements Serializable {
    private String result;
}
