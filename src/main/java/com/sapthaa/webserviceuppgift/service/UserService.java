package com.sapthaa.webserviceuppgift.service;

import com.sapthaa.webserviceuppgift.model.CustomUser;
import com.sapthaa.webserviceuppgift.movieRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
        return userRepository.findAll(); // Returnera alla användare
    }

    public String registerUser(CustomUser customUser) {
        // Validera om användarnamnet redan finns
        if (userRepository.findByUsername(customUser.getUsername()).isPresent()) {
            return "Användaren finns redan.";
        }

        String encodedPassword = passwordEncoder.encode(customUser.getPassword());
        customUser.setPassword(encodedPassword);

        // Spara användaren i databasen
        userRepository.save(customUser);

        return null;
    }

    public String deleteUser(String username) {
        // Kontrollera om användaren finns
        CustomUser user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return "Användaren finns inte.";
        }

        if (user.getRole() == "ADMIN"){
            return "Admin kan inte raderas";
        }
        // Ta bort användaren från databasen
        userRepository.delete(user);

        return "Användaren har tagits bort.";
    }



}