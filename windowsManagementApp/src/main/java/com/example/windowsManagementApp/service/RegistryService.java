package com.example.windowsManagementApp.service;

import com.example.windowsManagementApp.domain.AutoloadedApp;
import com.example.windowsManagementApp.domain.RestrictedApp;
import com.example.windowsManagementApp.domain.User;
import com.example.windowsManagementApp.repos.AutoloadedAppRepository;
import com.example.windowsManagementApp.repos.RestrictedAppRepository;
import com.example.windowsManagementApp.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Service
public class RegistryService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AutoloadedAppRepository autoloadedAppRepository;

    @Autowired
    RestrictedAppRepository restrictedAppRepository;


    public void addUser(User user) throws IOException {

        User windowsUser = userRepository.save(user);
        String command = String.format("net user " + windowsUser.getUsername() + " " + windowsUser.getPassword() + " /add");
        System.out.println(command);
        Runtime.getRuntime().exec(command);


        if ("Administrator".equalsIgnoreCase(user.getRole())) {
            Runtime.getRuntime().exec(String.format("net localgroup Администраторы " + windowsUser.getUsername() + " /add"));
        } else if ("Standard".equalsIgnoreCase(user.getRole())) {
            Runtime.getRuntime().exec(String.format("net localgroup Пользователи " + windowsUser.getUsername() + " /add"));
        } else if ("Guest".equalsIgnoreCase(user.getRole())) {
            Runtime.getRuntime().exec(String.format("net localgroup Гости " + windowsUser.getUsername() + " /add"));
        }

    }


    public void updateUser(User user) throws IOException {

        userRepository.save(user);
        String command = String.format("net user " + user.getUsername() + " " + user.getPassword() + " /add");
        Runtime.getRuntime().exec(command);


        if ("Administrator".equalsIgnoreCase(user.getRole())) {
            Runtime.getRuntime().exec(String.format("net localgroup Администраторы" + user.getUsername() + " /add"));
        } else if ("Standard".equalsIgnoreCase(user.getRole())) {
            Runtime.getRuntime().exec(String.format("net localgroup Пользователи " + user.getUsername() + " /add"));
        } else if ("Guest".equalsIgnoreCase(user.getRole())) {
            Runtime.getRuntime().exec(String.format("net localgroup Гости " + user.getUsername() + " /add"));
        }

    }


    public void deleteUser(int userId) throws IOException {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }


        String command = String.format("net user %s /delete", user.getUsername());
        Runtime.getRuntime().exec(command);


        userRepository.deleteById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public void addAutoloadedApp(AutoloadedApp app) throws IOException {
        if (app.getAppPath() == null || app.getAppPath().isEmpty()) {
            throw new IllegalArgumentException("App path must not be empty");
        }


        String command = String.format(
                "reg add HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v \"%s\" /t REG_SZ /d \"%s\" /f",
                app.getAppName(),
                app.getAppPath()
        );
        Runtime.getRuntime().exec(command);
        autoloadedAppRepository.save(app);
    }



    public void deleteAutoloadedApp(int appId, String appName) throws IOException {
        if (appId == 0 && appName != null) {

            String command = String.format(
                    "reg delete HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v \"%s\" /f",
                    appName
            );
            Runtime.getRuntime().exec(command);
        } else {
            throw new RuntimeException("Invalid parameters for deleting an autoloaded app");
        }
        autoloadedAppRepository.deleteById(appId);
    }

    public List<AutoloadedApp> getAllAutoloadedApps() throws IOException {
        List<AutoloadedApp> apps = new ArrayList<>();


        String command = "reg query HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("REG_SZ")) {
                String[] parts = line.trim().split("\\s{4,}"); // Split by multiple spaces
                if (parts.length >= 3) {
                    AutoloadedApp app = new AutoloadedApp();
                    app.setAppName(parts[0]);
                    app.setAppPath(parts[2]);
                    apps.add(app);
                }
            }
        }
        reader.close();
        return apps;
    }


    public void addRestrictedApp(RestrictedApp app) throws IOException {
        if (app.getAppName() == null || app.getAppPath() == null || app.getAppPath().isEmpty()) {
            throw new IllegalArgumentException("App name and path must not be empty");
        }


        String explorerPath = "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\Explorer";
        String disallowRunPath = explorerPath + "\\DisallowRun";


        String enablePolicyCommand = String.format(
                "reg add " + explorerPath + " /v DisallowRun /t REG_DWORD /d 1 /f");
        Runtime.getRuntime().exec(enablePolicyCommand);


        String addAppCommand = String.format(
                "reg add " + disallowRunPath + " /v " + app.getAppName() + " /t REG_SZ /d " + app.getAppPath() + " /f");
        Runtime.getRuntime().exec(addAppCommand);

restrictedAppRepository.save(app);
    }


    public void deleteRestrictedApp(String appName) throws IOException {
        if (appName == null || appName.isEmpty()) {
            throw new IllegalArgumentException("App name must not be empty");
        }


        String disallowRunPath = "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\Explorer\\DisallowRun";


        String deleteAppCommand = String.format(
                "reg delete \"%s\" /v \"%s\" /f", disallowRunPath, appName);

        Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", deleteAppCommand});


        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();


        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            while ((line = errorReader.readLine()) != null) {
                System.err.println("Error: " + line);
            }
        }

        System.out.println("Deleted restricted app successfully: " + appName);
       RestrictedApp app = restrictedAppRepository.findByAppName(appName);
       restrictedAppRepository.deleteById(app.getId());
    }


    public List<RestrictedApp> getRestrictedApps() throws IOException {
        List<RestrictedApp> apps = new ArrayList<>();


        String disallowRunPath = "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\Explorer\\DisallowRun";
        String command = String.format("reg query \"%s\"", disallowRunPath);


        Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", command});
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();


            if (!line.isEmpty() && line.contains("REG_SZ")) {
                String[] parts = line.split("\\s{4,}");
                if (parts.length >= 2) {
                    RestrictedApp app = new RestrictedApp();
                    app.setAppName(parts[0].trim());
                    app.setAppPath(parts[1].trim());
                    apps.add(app);
                }
            }
        }
        reader.close();
        return apps;
    }


}

