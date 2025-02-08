package com.sapthaa.webserviceuppgift.movieRepository;

import com.sapthaa.webserviceuppgift.model.CustomUser;
import com.sapthaa.webserviceuppgift.service.LoggerService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DatabaseInit {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoggerService logger;

    @Autowired
    public DatabaseInit(UserRepository userRepository, PasswordEncoder passwordEncoder, LoggerService logger) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.logger = logger;
    }

    @PostConstruct
    public void createUser() {
        try {
            if (userRepository.count() == 0) {
                logger.info("No users found in the database. Creating an admin user...");

                CustomUser user = new CustomUser();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setRole("ADMIN");

                userRepository.save(user);
                logger.info("Admin user created successfully.");
            } else {
                logger.info("Users already exist in the database. Skipping user creation.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while initializing the database: {}" + e.getMessage(), e);
        }
    }
}
