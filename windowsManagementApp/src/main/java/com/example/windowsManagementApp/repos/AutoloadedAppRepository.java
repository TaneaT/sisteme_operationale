package com.example.windowsManagementApp.repos;

import com.example.windowsManagementApp.domain.AutoloadedApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoloadedAppRepository extends JpaRepository<AutoloadedApp, Integer> {

}