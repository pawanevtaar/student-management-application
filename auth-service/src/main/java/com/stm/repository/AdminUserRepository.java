package com.stm.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stm.entity.AdminUser;



public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByUsername(String username);
}