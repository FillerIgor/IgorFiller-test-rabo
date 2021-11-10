package com.rabobank.customerstateprocessor.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
public class CustomerStateEntity {
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
