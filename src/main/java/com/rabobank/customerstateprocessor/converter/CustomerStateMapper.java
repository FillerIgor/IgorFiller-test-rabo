package com.rabobank.customerstateprocessor.converter;

import com.rabobank.customerstateprocessor.dto.CustomerStateDto;
import com.rabobank.customerstateprocessor.dto.fileContent.csv.CustomerStateRecordCSVDto;
import com.rabobank.customerstateprocessor.dto.fileContent.xml.CustomerStateRecordXMLDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CustomerStateMapper {
    public CustomerStateDto convertCsvDataToCustomerState(CustomerStateRecordCSVDto customerStateCSVDto) {
        return CustomerStateDto.builder()
                .recordReference(customerStateCSVDto.getRecordReference())
                .accountNumber(customerStateCSVDto.getAccountNumber())
                .description(customerStateCSVDto.getDescription())
                .startBalance(new BigDecimal(customerStateCSVDto.getStartBalance()))
                .mutation(new BigDecimal(customerStateCSVDto.getMutation()))
                .endBalance(new BigDecimal(customerStateCSVDto.getEndBalance()))
                .build();

    }

    public CustomerStateDto convertXmlDataToCustomerState(CustomerStateRecordXMLDto customerStateXMLDto) {
        return CustomerStateDto.builder()
                .recordReference(customerStateXMLDto.getRecordReference())
                .accountNumber(customerStateXMLDto.getAccountNumber())
                .description(customerStateXMLDto.getDescription())
                .startBalance(new BigDecimal(customerStateXMLDto.getStartBalance()))
                .mutation(new BigDecimal(customerStateXMLDto.getMutation()))
                .endBalance(new BigDecimal(customerStateXMLDto.getEndBalance()))
                .build();
    }
}
