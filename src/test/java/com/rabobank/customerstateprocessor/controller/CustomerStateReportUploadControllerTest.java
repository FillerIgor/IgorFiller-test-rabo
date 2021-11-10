package com.rabobank.customerstateprocessor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.customerstateprocessor.CustomerStateProcessorApplication;
import com.rabobank.customerstateprocessor.controller.response.UploadStatusResponse;
import com.rabobank.customerstateprocessor.dto.InvalidCustomerStateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CustomerStateProcessorApplication.class
)
class CustomerStateReportUploadControllerTest {

    @Autowired
    private CustomerStateReportUploadController controller;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldUploadCsvFile() throws Exception {
        //given
        MockMultipartFile fileUnderTest = new MockMultipartFile("file", new ClassPathResource("records.csv").getInputStream());
        String expectedResult = objectMapper.writeValueAsString(UploadStatusResponse.builder()
                .uploadedTransferReferenceNumbers(List.of("132843", "107934", "191708", "191476",
                        "139812", "192851", "187948"))
                .invalidTransfers(List.of(
                        InvalidCustomerStateDto.builder()
                                .transactionReference("112806")
                                .reason("Transaction Reference number is not unique")
                                .build()
                ))
                .build());
        //when
        mvc.perform(multipart("/upload/CSV")
                        .file(fileUnderTest))
                //then
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    void shouldUploadXmlFile() throws Exception {
        //given
        MockMultipartFile fileUnderTest = new MockMultipartFile("file", new ClassPathResource("records.xml").getInputStream());
        String expectedResult = objectMapper.writeValueAsString(UploadStatusResponse.builder()
                .uploadedTransferReferenceNumbers(List.of("108366", "104254", "198725", "173553", "117057", "176709",
                        "192105", "190018"))
                .invalidTransfers(List.of(
                        InvalidCustomerStateDto.builder()
                                .transactionReference("108338")
                                .reason("The end balance is invalid. End balance is not equal to start balance and applied mutation")
                                .build(),
                        InvalidCustomerStateDto.builder()
                                .transactionReference("196839")
                                .reason("The end balance is invalid. End balance is not equal to start balance and applied mutation")
                                .build()
                ))
                .build());
        //when
        mvc.perform(multipart("/upload/XML")
                        .file(fileUnderTest))
                //then
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }
}