package com.rabobank.customerstateprocessor.service.validator;

import com.rabobank.customerstateprocessor.dto.CustomerStateDto;
import com.rabobank.customerstateprocessor.dto.InvalidCustomerStateDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerStateValidatorTest {

    private CustomerStateValidator sut = new CustomerStateValidator();

    @Test
    void shouldReturnEmptyListWhenAllTransactionsValid() {
        //given
        List<CustomerStateDto> testData = generateTestData();
        //when
        Set<InvalidCustomerStateDto> result = sut.validate(testData);
        //then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnListWithInvalidTransfersByReferenceNumberValidation() {
        //given
        List<CustomerStateDto> testData = generateTestData();
        testData.add(CustomerStateDto.builder().recordReference("id_1").startBalance(BigDecimal.ZERO)
                .mutation(BigDecimal.ZERO)
                .endBalance(BigDecimal.ZERO).build());
        testData.add(CustomerStateDto.builder().recordReference("id_1").startBalance(BigDecimal.ZERO)
                .mutation(BigDecimal.ZERO)
                .endBalance(BigDecimal.ZERO).build());
        //when
        Set<InvalidCustomerStateDto> result = sut.validate(testData);
        //then
        assertThat(testData).filteredOn(data -> "id_1".equals(data.getRecordReference())).hasSize(3);
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result).flatExtracting(InvalidCustomerStateDto::getTransactionReference, InvalidCustomerStateDto::getReason)
                .containsExactly("id_1", "Transaction Reference number is not unique");
    }

    @Test
    void shouldReturnListWithInvalidTransfersByEndBalanceValidation() {
        //given
        List<CustomerStateDto> testData = generateTestData();
        testData.get(0).setEndBalance(BigDecimal.TEN);
        //when
        Set<InvalidCustomerStateDto> result = sut.validate(testData);
        //then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result).flatExtracting(InvalidCustomerStateDto::getTransactionReference, InvalidCustomerStateDto::getReason)
                .containsExactly("id_0", "The end balance is invalid. End balance is not equal to start balance and applied mutation");
    }

    private List<CustomerStateDto> generateTestData() {
        return IntStream.range(0, 10).mapToObj(i -> CustomerStateDto.builder()
                .recordReference("id_" + i)
                .startBalance(BigDecimal.ZERO)
                .mutation(BigDecimal.ZERO)
                .endBalance(BigDecimal.ZERO)
                .build()).collect(Collectors.toList());
    }
}