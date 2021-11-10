package com.rabobank.customerstateprocessor.service.validator;

import com.rabobank.customerstateprocessor.dto.CustomerStateDto;
import com.rabobank.customerstateprocessor.dto.InvalidCustomerStateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomerStateValidator {

    public Set<InvalidCustomerStateDto> validate(List<CustomerStateDto> customerStates) {
        Set<InvalidCustomerStateDto> invalidCustomerStates = new HashSet<>();
        invalidCustomerStates.addAll(isReferenceNumberValid(customerStates));
        for (CustomerStateDto customerState : customerStates) {
            if (!isEndBalanceValid(customerState)) {
                invalidCustomerStates.add(InvalidCustomerStateDto.builder()
                        .transactionReference(customerState.getRecordReference())
                        .reason("The end balance is invalid. End balance is not equal to start balance and applied mutation")
                        .build());
            }
        }
        return invalidCustomerStates;
    }

    private Set<InvalidCustomerStateDto> isReferenceNumberValid(List<CustomerStateDto> customerStates) {
        return customerStates.stream()
                .collect(Collectors.groupingBy(CustomerStateDto::getRecordReference, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> InvalidCustomerStateDto.builder()
                        .transactionReference(entry.getKey())
                        .reason("Transaction Reference number is not unique")
                        .build())
                .collect(Collectors.toSet());
    }

    private boolean isEndBalanceValid(CustomerStateDto customerState) {
        if (Objects.isNull(customerState.getStartBalance()) ||
                Objects.isNull(customerState.getMutation()) ||
                Objects.isNull(customerState.getEndBalance())) {
            log.debug("Provided balance properties or mutation is null for {}", customerState.getRecordReference());
            return false;
        }
        return customerState.getEndBalance().equals(
                customerState.getStartBalance().add(customerState.getMutation()));
    }
}
