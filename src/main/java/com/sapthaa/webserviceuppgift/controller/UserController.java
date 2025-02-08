package com.sapthaa.webserviceuppgift.controller;

import com.sapthaa.webserviceuppgift.model.CustomUser;
import com.sapthaa.webserviceuppgift.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // registreringsida
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("customUser", new CustomUser());
        return "register";
    }

    // registrera en användare
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("customUser") CustomUser customUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Det finns fel i formuläret. Vänligen rätta till dem.");
            return "register"; //
        }

        String result = userService.registerUser(customUser);
        if (result != null) {
            model.addAttribute("error", result); // Om registreringen misslyckas
            return "register";
        }

        // Om registreringen lyckas
        model.addAttribute("success", "Registreringen lyckades!");
        return "registersuccess"; // Omregistrering lyckades
    }

    // admin sida
    @GetMapping("/admin")
    public String showUserManagement(Model model) {
        model.addAttribute("users", userService.getAllUsers()); // Lista alla användare
        return "admin";
    }

    // ta bort en användare
    @PostMapping("/admin/delete-user/{username}")
    public String deleteUser(@PathVariable String username, Model model) {
        String result = userService.deleteUser(username);
        if (result != null) {
            model.addAttribute("error", result);
            return "admin";
        }
        model.addAttribute("success", "Användaren har tagits bort.");
        return "admin";
    }
    

}
