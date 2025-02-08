package com.sapthaa.webserviceuppgift.movieRepository;
import com.sapthaa.webserviceuppgift.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    Optional<Movie> findById(Long id);
}
