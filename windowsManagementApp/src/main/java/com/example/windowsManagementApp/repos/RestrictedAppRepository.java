package com.example.windowsManagementApp.repos;

import com.example.windowsManagementApp.domain.RestrictedApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestrictedAppRepository extends JpaRepository<RestrictedApp, Integer> {
RestrictedApp findByAppName(String appName);
}
