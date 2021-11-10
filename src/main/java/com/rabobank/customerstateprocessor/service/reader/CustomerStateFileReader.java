package com.rabobank.customerstateprocessor.service.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabobank.customerstateprocessor.controller.FileType;
import com.rabobank.customerstateprocessor.converter.CustomerStateMapper;
import com.rabobank.customerstateprocessor.dto.CustomerStateDto;
import com.rabobank.customerstateprocessor.dto.fileContent.csv.CustomerStateRecordCSVDto;
import com.rabobank.customerstateprocessor.dto.fileContent.xml.CustomerStateXMLDto;
import com.rabobank.customerstateprocessor.exception.CustomerStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.rabobank.customerstateprocessor.controller.FileType.CSV;
import static com.rabobank.customerstateprocessor.controller.FileType.XML;

@Component
public class CustomerStateFileReader {

    private final Map<FileType, FileReader> fileReaders;
    private final CustomerStateMapper customerStateMapper;

    public CustomerStateFileReader(@Autowired List<FileReader> fileReaders,
                                   @Autowired CustomerStateMapper customerStateMapper) {
        this.fileReaders = fileReaders.stream()
                .collect(Collectors.toMap(FileReader::getFileType, Function.identity()));
        this.customerStateMapper = customerStateMapper;
    }

    public List<CustomerStateDto> readFile(byte[] fileContent, FileType fileType) throws IOException {
        switch (fileType) {
            case CSV:
                List<CustomerStateRecordCSVDto> customerStateCSVDto = ((CSVReader) fileReaders.get(CSV)).readFile(fileContent, new TypeReference<List<CustomerStateRecordCSVDto>>() {
                });
                return customerStateCSVDto.stream().map(customerStateMapper::convertCsvDataToCustomerState).collect(Collectors.toList());
            case XML:
                CustomerStateXMLDto customerStateXMLDto = ((XMLReader) fileReaders.get(XML)).readFile(fileContent, CustomerStateXMLDto.class);
                return customerStateXMLDto.getRecords().stream().map(customerStateMapper::convertXmlDataToCustomerState).collect(Collectors.toList());
            default:
                throw new CustomerStateException("Unsupported file extension");
        }
    }
}
