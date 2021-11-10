package com.rabobank.customerstateprocessor.service.reader;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.rabobank.customerstateprocessor.controller.FileType;
import com.rabobank.customerstateprocessor.dto.fileContent.xml.CustomerStateXMLDto;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class XMLReader implements FileReader {

    private final XmlMapper xmlMapper = new XmlMapper();

    public CustomerStateXMLDto readFile(byte[] fileContent, Class<CustomerStateXMLDto> targetClass) throws IOException {
        return xmlMapper.readValue(fileContent, targetClass);
    }

    @Override
    public FileType getFileType() {
        return FileType.XML;
    }
}
