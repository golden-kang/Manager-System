package com.golden.ms.exceptions.handlers;

import com.golden.ms.exceptions.model.*;
import com.golden.ms.utils.CodeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(ForbiddenException.class)
    @ResponseBody
    public ResponseEntity<?> forbiddenExceptionHandler(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(CodeUtils.serializeErrorResponse(HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseBody
    public ResponseEntity<?> alreadyExistExceptionHandler(AlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(CodeUtils.serializeErrorResponse(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<?> alreadyExistExceptionHandler(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CodeUtils.serializeErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity<?> NotFoundExceptionHandler(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CodeUtils.serializeErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
    }

}
