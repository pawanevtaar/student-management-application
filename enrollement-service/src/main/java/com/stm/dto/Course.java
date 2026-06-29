
package com.stm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter

@Setter
public class Course {

	private Long id;
	private String courseName;
	private String description;
	private String courseType;
	private Integer duration;
	private String topics;
	private Double courseFees;
}
