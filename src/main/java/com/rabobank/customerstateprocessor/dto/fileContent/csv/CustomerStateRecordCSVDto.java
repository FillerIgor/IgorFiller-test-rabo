package com.rabobank.customerstateprocessor.dto.fileContent.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({
        "Reference",
        "Account Number",
        "Description",
        "Start Balance",
        "Mutation",
        "End Balance"
})
public class CustomerStateRecordCSVDto {
    @JsonProperty("Reference")
    private String recordReference;
    @JsonProperty("Account Number")
    private String accountNumber;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Start Balance")
    private String startBalance;
    @JsonProperty("Mutation")
    private String mutation;
    @JsonProperty("End Balance")
    private String endBalance;
}
