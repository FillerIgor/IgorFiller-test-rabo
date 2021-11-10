package com.rabobank.customerstateprocessor.controller.response;

import com.rabobank.customerstateprocessor.dto.InvalidCustomerStateDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class UploadStatusResponse extends CustomerStateResponse {
    private Collection<String> uploadedTransferReferenceNumbers;
    private Collection<InvalidCustomerStateDto> invalidTransfers;
}
