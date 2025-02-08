package com.sapthaa.webserviceuppgift.movieRepository;

import com.sapthaa.webserviceuppgift.model.CustomUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DatabaseInit {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createUser () throws IOException {
        if (userRepository.count() == 0) {
            CustomUser user = new CustomUser();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRole("ADMIN");
            userRepository.save(user);
        }
    }
}
