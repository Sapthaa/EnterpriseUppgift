package com.sapthaa.webserviceuppgift.service;

import com.sapthaa.webserviceuppgift.model.Movie;
import com.sapthaa.webserviceuppgift.movieRepository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieWebService {

    private final MovieRepository movieRepository;

    public MovieWebService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMoviesForWeb() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElse(null);
    }

    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    public String updateMovieOverview(Movie movie) {
        movie.setOverview(movie.getOverview() + ", " + movie.getTitle());
        return movieRepository.saveAndFlush(movie).getTitle();
    }

}