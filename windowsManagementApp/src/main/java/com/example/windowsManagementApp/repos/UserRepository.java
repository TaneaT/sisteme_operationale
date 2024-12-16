package com.example.windowsManagementApp.repos;
import com.example.windowsManagementApp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


import com.example.windowsManagementApp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}

