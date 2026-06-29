package com.stm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.stm.dto.Student;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;



@FeignClient(name = "student-service")
public interface StudentClient {

    @GetMapping("/{id}")
    Student getStudent(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    );
}