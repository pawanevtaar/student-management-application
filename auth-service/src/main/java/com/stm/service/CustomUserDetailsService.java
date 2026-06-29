package com.stm.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stm.entity.AdminUser;
import com.stm.entity.Student;
import com.stm.repository.AdminUserRepository;
import com.stm.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // Check Admin
        Optional<AdminUser> admin =
                adminUserRepository.findByUsername(username);

        if (admin.isPresent()) {
            return User.builder()
                    .username(admin.get().getUsername())
                    .password(admin.get().getPassword())
                    .roles("ADMIN")
                    .build();
        }

        // Check Student
        Optional<Student> student =
                studentRepository.findByStudentCode(username);

        if (student.isPresent()) {
            return User.builder()
                    .username(student.get().getStudentCode())
                    .password(student.get().getDateOfBirth().toString()) 
                    // or encoded password if you store one
                    .roles("STUDENT")
                    .build();
        }

        throw new UsernameNotFoundException("User not found");
    }
}