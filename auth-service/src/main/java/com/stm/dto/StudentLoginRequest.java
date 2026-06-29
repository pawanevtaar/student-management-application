package com.stm.dto;



import java.time.LocalDate;

import lombok.Data;

@Data
public class StudentLoginRequest {

    private String studentCode;
    private LocalDate dateOfBirth;
}