package com.rabobank.customerstateprocessor.controller.exception;

import com.rabobank.customerstateprocessor.controller.response.ErrorResponse;
import com.rabobank.customerstateprocessor.exception.CustomerStateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class CustomerStateControllerAdvice {

    @ExceptionHandler({CustomerStateException.class})
    public ResponseEntity<Object> handleCustomerStateException(CustomerStateException ex) {
        //no need to log every 4xx Client response with warn or higher log level
        log.debug(String.format("Exception occurs with [message=%s]", ex.getMessage()), ex);
        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .errorCode(BAD_REQUEST.value())
                        .errorMessage(ex.getMessage())
                        .build());
    }

}
