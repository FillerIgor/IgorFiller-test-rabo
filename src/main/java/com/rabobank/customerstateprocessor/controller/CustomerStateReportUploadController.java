package com.rabobank.customerstateprocessor.controller;

import com.rabobank.customerstateprocessor.controller.response.CustomerStateResponse;
import com.rabobank.customerstateprocessor.controller.response.ErrorResponse;
import com.rabobank.customerstateprocessor.controller.response.UploadStatusResponse;
import com.rabobank.customerstateprocessor.exception.CustomerStateException;
import com.rabobank.customerstateprocessor.service.CustomerStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.BiFunction;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequiredArgsConstructor
public class CustomerStateReportUploadController {

    private final CustomerStateService customerStateService;

    @PostMapping(value = "/upload/{fileType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerStateResponse> uploadCustomerState(@RequestParam MultipartFile file, @PathVariable("fileType") FileType fileType) {
        //todo check file extension - CSV, XML
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ErrorResponse.builder()
                    //todo move message to class with constants
                    .errorMessage("The file to validate and upload is empty")
                    .errorCode(BAD_REQUEST.value())
                    .build());
        } else {
            return ResponseEntity.ok(throwingUploadFileExecution(customerStateService::uploadFile, file, fileType));
        }
    }

    private CustomerStateResponse throwingUploadFileExecution(BiFunction<byte[], FileType, UploadStatusResponse> methodToExecute, MultipartFile fileContent, FileType fileType) {
        try {
            return methodToExecute.apply(fileContent.getBytes(), fileType);
        } catch (IOException e) {
            throw new CustomerStateException("Access to file errors occurs", e);
        }
    }
}
