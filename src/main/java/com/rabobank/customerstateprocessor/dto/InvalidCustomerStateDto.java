package com.rabobank.customerstateprocessor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvalidCustomerStateDto {
    private String transactionReference;
    private String reason;
}
