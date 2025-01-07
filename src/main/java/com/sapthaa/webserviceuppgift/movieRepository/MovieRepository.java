package com.sapthaa.webserviceuppgift.movieRepository;
import com.sapthaa.webserviceuppgift.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
}
