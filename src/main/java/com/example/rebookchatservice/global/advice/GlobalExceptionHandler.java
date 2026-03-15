package com.example.rebookchatservice.global.advice;

import com.example.rebookchatservice.common.CommonResult;
import com.example.rebookchatservice.common.ResponseService;
import com.example.rebookchatservice.global.exception.CDuplicatedDataException;
import com.example.rebookchatservice.global.exception.CMissingDataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CMissingDataException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult missingDataException(CMissingDataException e) {
        return ResponseService.getFailResult(e);
    }

    @ExceptionHandler(CDuplicatedDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResult duplicatedDataException(CDuplicatedDataException e) {
        return ResponseService.getFailResult(e);
    }
}
