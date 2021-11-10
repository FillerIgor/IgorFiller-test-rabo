package com.rabobank.customerstateprocessor.service;

import com.rabobank.customerstateprocessor.controller.FileType;
import com.rabobank.customerstateprocessor.controller.response.UploadStatusResponse;
import com.rabobank.customerstateprocessor.dto.CustomerStateDto;
import com.rabobank.customerstateprocessor.dto.InvalidCustomerStateDto;
import com.rabobank.customerstateprocessor.exception.CustomerStateException;
import com.rabobank.customerstateprocessor.service.reader.CustomerStateFileReader;
import com.rabobank.customerstateprocessor.service.validator.CustomerStateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerStateService {

    private final CustomerStateFileReader fileReader;
    private final CustomerStateValidator validator;

    public UploadStatusResponse uploadFile(byte[] fileContent, FileType fileType) {
        try {
            final List<CustomerStateDto> data = fileReader.readFile(fileContent, fileType);
            Set<InvalidCustomerStateDto> invalidCustomerStates = validator.validate(data);
            return UploadStatusResponse.builder()
                    .uploadedTransferReferenceNumbers(calculateAndSaveValidTransfers(data, invalidCustomerStates))
                    .invalidTransfers(invalidCustomerStates)
                    .build();
        } catch (IOException e) {
            throw new CustomerStateException("Unable to read file", e);
        }
    }

    private List<String> calculateAndSaveValidTransfers(List<CustomerStateDto> transfers, Collection<InvalidCustomerStateDto> invalidTransfers) {
        Set<String> invalidTransferNumbers = invalidTransfers.stream()
                .map(InvalidCustomerStateDto::getTransactionReference)
                .collect(Collectors.toSet());
        return transfers.stream()
                .filter(transfer -> transfer.getRecordReference() != null && !invalidTransferNumbers.contains(transfer.getRecordReference()))
//                .map() todo add save to H2 in-memory DB
                .map(CustomerStateDto::getRecordReference)
                .collect(Collectors.toList());
    }
}
