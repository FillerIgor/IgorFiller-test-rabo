package com.rabobank.customerstateprocessor.service.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.rabobank.customerstateprocessor.controller.FileType;
import com.rabobank.customerstateprocessor.dto.fileContent.csv.CustomerStateRecordCSVDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CSVReader implements FileReader {

    private final CsvMapper csvMapper = new CsvMapper();

    public List<CustomerStateRecordCSVDto> readFile(byte[] fileContent, TypeReference<List<CustomerStateRecordCSVDto>> targetClass) throws IOException {
        return csvMapper.readerFor(CustomerStateRecordCSVDto.class)
                .with(csvMapper.typedSchemaFor(CustomerStateRecordCSVDto.class)
                        .withHeader()) //todo add ability to configure is header included
                .readValues(new String(fileContent)).readAll()
                .stream()
                .map(row -> (CustomerStateRecordCSVDto) row)
                .collect(Collectors.toList());
    }

    @Override
    public FileType getFileType() {
        return FileType.CSV;
    }
}
