
package com.stm.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter

@Setter
public class Student {

	private Long id;
	private String name;
	private LocalDate dateOfBirth;
	private String gender;
	private String studentCode;
	private String email;
	private String mobileNumber;
	private String fatherName;
	private String motherName;
}
