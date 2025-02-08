package com.sapthaa.webserviceuppgift.controller;

import com.sapthaa.webserviceuppgift.model.CustomUser;
import com.sapthaa.webserviceuppgift.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // Registreringsida
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        logger.info("Visar registreringsformuläret");

        try {
            model.addAttribute("customUser", new CustomUser());
            return "register";
        } catch (Exception e) {
            logger.error("Ett fel uppstod vid laddning av registreringssidan: {}", e.getMessage(), e);
            model.addAttribute("error", "Kunde inte ladda registreringssidan.");
            return "error";
        }
    }

    // Registrera en användare
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("customUser") CustomUser customUser, BindingResult bindingResult, Model model) {
        logger.info("Försöker registrera användare: {}", customUser.getUsername());

        try {
            if (bindingResult.hasErrors()) {
                logger.warn("Formulärfel vid registrering av användare: {}", customUser.getUsername());
                model.addAttribute("error", "Det finns fel i formuläret. Vänligen rätta till dem.");
                return "register";
            }

            String result = userService.registerUser(customUser);
            if (result != null) {
                logger.warn("Registrering misslyckades för användare: {} - Felmeddelande: {}", customUser.getUsername(), result);
                model.addAttribute("error", result);
                return "register";
            }

            logger.info("Registrering lyckades för användare: {}", customUser.getUsername());
            model.addAttribute("success", "Registreringen lyckades!");
            return "registersuccess";

        } catch (Exception e) {
            logger.error("Ett fel uppstod vid registrering av användare: {} - Fel: {}", customUser.getUsername(), e.getMessage(), e);
            model.addAttribute("error", "Ett internt fel uppstod vid registrering.");
            return "error";
        }
    }

    // Admin-sida
    @GetMapping("/admin")
    public String showUserManagement(Model model) {
        logger.info("Visar admin-sidan med alla användare");

        try {
            model.addAttribute("users", userService.getAllUsers());
            return "admin";
        } catch (Exception e) {
            logger.error("Ett fel uppstod vid hämtning av användare: {}", e.getMessage(), e);
            model.addAttribute("error", "Kunde inte ladda admin-sidan.");
            return "error";
        }
    }

    // Ta bort en användare
    @PostMapping("/admin/delete-user/{username}")
    public String deleteUser(@PathVariable String username, Model model) {
        logger.info("Försöker ta bort användare: {}", username);

        try {
            String result = userService.deleteUser(username);
            if (result != null) {
                logger.warn("Misslyckades med att ta bort användare: {} - Felmeddelande: {}", username, result);
                model.addAttribute("error", result);
                return "admin";
            }

            logger.info("Användaren {} har tagits bort", username);
            model.addAttribute("success", "Användaren har tagits bort.");
            return "admin";

        } catch (Exception e) {
            logger.error("Ett fel uppstod vid borttagning av användare: {} - Fel: {}", username, e.getMessage(), e);
            model.addAttribute("error", "Ett internt fel uppstod vid borttagning.");
            return "error";
        }
    }
}