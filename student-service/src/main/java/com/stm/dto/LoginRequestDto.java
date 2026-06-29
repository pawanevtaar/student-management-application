package com.stm.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String studentCode;
    private LocalDate dateOfBirth;
}