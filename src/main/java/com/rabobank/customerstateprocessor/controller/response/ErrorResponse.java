package com.rabobank.customerstateprocessor.controller.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ErrorResponse extends CustomerStateResponse {
    private String errorMessage;
    private Integer errorCode;
}
