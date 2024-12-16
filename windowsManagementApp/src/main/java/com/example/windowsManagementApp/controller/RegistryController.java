package com.example.windowsManagementApp.controller;

import com.example.windowsManagementApp.domain.AutoloadedApp;
import com.example.windowsManagementApp.domain.RestrictedApp;
import com.example.windowsManagementApp.domain.User;
import com.example.windowsManagementApp.repos.UserRepository;
import com.example.windowsManagementApp.service.RegistryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Controller
public class RegistryController {

    @Autowired
    private RegistryService registryService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }


    @GetMapping("/users/view")
    public String viewUsers(Model model) {
        List<User> user = registryService.getAllUsers();
        model.addAttribute("user", user);
        return "viewUsers";
    }

    @GetMapping("/users/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user) throws IOException {
        registryService.addUser(user);
        return "redirect:/users/view";
    }

    @GetMapping("/users/modify/{id}")
    public String modifyUserForm(@PathVariable int id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);

        return "modifyUser";
    }

    @PostMapping("/users/modify")
    public String modifyUser(@ModelAttribute User user) {
        try {
            registryService.updateUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/users/view";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam int id) {
        try {
            registryService.deleteUser(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/users/view";
    }


    @GetMapping("/registry/autoload/add")
    public String addAutoloadedAppForm(Model model) {
        model.addAttribute("autoloadedApp", new AutoloadedApp());
        return "addAutoloadedApp";
    }

    @GetMapping("/registry/autoload/view/")
    public String viewAutoloadedApps(Model model) {
        try {
            List<AutoloadedApp> apps = registryService.getAllAutoloadedApps();
            model.addAttribute("apps", apps);
        } catch (IOException e) {
            model.addAttribute("error", "Failed to load registry apps: " + e.getMessage());
        }
        return "viewAutoloadedApps";
    }

    @PostMapping("/registry/autoload/add")
    public String addAutoloadedApp(@ModelAttribute AutoloadedApp autoloadedApp) {
        try {
            registryService.addAutoloadedApp(autoloadedApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/registry/autoload/view/";
    }

    @PostMapping("/registry/autoload/delete")
    public String deleteAutoloadedApp(@RequestParam String appName) {
        try {
            registryService.deleteAutoloadedApp(0, appName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/registry/autoload/view/";
    }


    @GetMapping("/registry/restrict/view/")
    public String viewRestrictedApps(Model model) {
        try {
            List<RestrictedApp> apps = registryService.getRestrictedApps();
            model.addAttribute("apps", apps);
        } catch (IOException e) {
            model.addAttribute("error", "Failed to load restricted apps: " + e.getMessage());
        }
        return "viewRestrictedApps";
    }

    @GetMapping("/registry/restrict/add")
    public String addRestrictedAppForm(Model model) {
        model.addAttribute("restrictedApp", new RestrictedApp());
        return "addRestrictedApp";
    }

    @PostMapping("/registry/restrict/add")
    public String addRestrictedApp(@ModelAttribute RestrictedApp restrictedApp) {
        try {
            registryService.addRestrictedApp(restrictedApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/registry/restrict/view/";
    }

    @PostMapping("/registry/restrict/delete")
    public String deleteRestrictedApp(@RequestParam String appName) {
        try {
            registryService.deleteRestrictedApp(appName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/registry/restrict/view/";
    }



}
