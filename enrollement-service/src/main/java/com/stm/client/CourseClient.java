package com.stm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.stm.dto.Course;



@FeignClient(name = "course-service")
public interface CourseClient {

    @GetMapping("/{id}")
    Course getCourse(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    );
}