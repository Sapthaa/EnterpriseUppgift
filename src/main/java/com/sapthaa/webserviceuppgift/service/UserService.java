package com.sapthaa.webserviceuppgift.service;

import com.sapthaa.webserviceuppgift.model.CustomUser;
import com.sapthaa.webserviceuppgift.movieRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomUser> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {

            throw new RuntimeException("fel med att hämta användare: " + e.getMessage());
        }
    }

    public String registerUser(CustomUser customUser) {
        try {
            // Validera om användarnamnet redan finns
            if (userRepository.findByUsername(customUser.getUsername()).isPresent()) {
                return "Användaren finns redan.";
            }

            String encodedPassword = passwordEncoder.encode(customUser.getPassword());
            customUser.setPassword(encodedPassword);

            // Spara användaren i databasen
            userRepository.save(customUser);
            return null;

        } catch (Exception e) {

            return "Fel vid registrering av användare: " + e.getMessage();
        }
    }

    public String deleteUser(String username) {
        try {
            // Kontrollera om användaren finns
            CustomUser user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                return "Användaren finns inte.";
            }

            // Ta bort användaren från databasen
            userRepository.delete(user);
            return "Användaren har tagits bort.";

        } catch (Exception e) {

            return "Fel vid borttagning av användare: " + e.getMessage();
        }
    }
}