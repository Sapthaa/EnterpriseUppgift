package com.sapthaa.webserviceuppgift.movieRepository;


import com.sapthaa.webserviceuppgift.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Integer> {
    Optional<CustomUser> findByUsername(String username);

}
