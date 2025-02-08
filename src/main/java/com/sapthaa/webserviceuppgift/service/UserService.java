package com.sapthaa.webserviceuppgift.service;

import com.sapthaa.webserviceuppgift.model.CustomUser;
import com.sapthaa.webserviceuppgift.movieRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoggerService logger;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, LoggerService logger) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.logger = logger;
    }

    public List<CustomUser> getAllUsers() {
        try {
            logger.info("Hämtar alla användare från databasen.");
            return userRepository.findAll();
        } catch (Exception e) {
            logger.error("Fel vid hämtning av användare: {}" + e.getMessage(), e);
            throw new RuntimeException("Fel vid hämtning av användare.", e);
        }
    }

    public String registerUser(CustomUser customUser) {
        try {
            if (userRepository.findByUsername(customUser.getUsername()).isPresent()) {
                logger.warn("Försök att registrera en användare som redan finns: {}" + customUser.getUsername());
                return "Användaren finns redan.";
            }

            String encodedPassword = passwordEncoder.encode(customUser.getPassword());
            customUser.setPassword(encodedPassword);

            userRepository.save(customUser);
            logger.info("Ny användare registrerad: {}" + customUser.getUsername());
            return "Registrering utfördes";

        } catch (Exception e) {
            logger.error("Fel vid registrering av användare: {}" + e.getMessage(), e);
            return "Fel vid registrering av användare.";
        }
    }

    public String deleteUser(String username) {
        try {
            CustomUser user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                logger.warn("Försök att ta bort en icke-existerande användare: {}" + username);
                return "Användaren finns inte.";
            }

            userRepository.delete(user);
            logger.info("Användare borttagen: {}" + username);
            return "Användaren har tagits bort.";

        } catch (Exception e) {
            logger.error("Fel vid borttagning av användare: {}" + e.getMessage(), e);
            return "Fel vid borttagning av användare.";
        }
    }
}
