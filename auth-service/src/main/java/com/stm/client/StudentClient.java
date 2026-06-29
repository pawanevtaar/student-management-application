package com.stm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stm.entity.Student;


@FeignClient(name = "STUDENT-SERVICE")
public interface StudentClient {

    @GetMapping("/validate")
    Student validateStudent(
            @RequestParam String studentCode,
            @RequestParam String dateOfBirth
    );
}
