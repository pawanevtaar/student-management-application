package com.stm.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stm.client.StudentClient;
import com.stm.dto.AdminLoginRequest;
import com.stm.dto.StudentLoginRequest;
import com.stm.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final StudentClient studentClient;
    @PostMapping("/admin/login")
    public String login(@RequestBody AdminLoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        return jwtUtil.generateToken(request.getUsername());
    }
    
    @PostMapping("/student/login")
    public String studentLogin(
            @RequestBody StudentLoginRequest request) {

    	studentClient.validateStudent(
    	        request.getStudentCode(),
    	        request.getDateOfBirth().toString()
    	);

        return jwtUtil.generateToken(request.getStudentCode());
    }
}
