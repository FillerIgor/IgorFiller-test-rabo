package com.rabobank.customerstateprocessor.dto.fileContent.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class CustomerStateRecordXMLDto {
    @JacksonXmlProperty(isAttribute = true, localName = "reference")
    private String recordReference;
    private String accountNumber;
    private String description;
    private String startBalance;
    private String mutation;
    private String endBalance;
}
