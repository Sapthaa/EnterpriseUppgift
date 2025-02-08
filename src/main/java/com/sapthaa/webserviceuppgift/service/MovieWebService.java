package com.sapthaa.webserviceuppgift.service;

import com.sapthaa.webserviceuppgift.model.Movie;
import com.sapthaa.webserviceuppgift.movieRepository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieWebService {

    private final LoggerService logger;
    private final MovieRepository movieRepository;

    public MovieWebService(LoggerService logger, MovieRepository movieRepository) {
        this.logger = logger;
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMoviesForWeb() {
        try {
            logger.info("Hämtar alla filmer...");
            List<Movie> movies = movieRepository.findAll();
            logger.info("Antal filmer hämtade: {}" + movies.size());
            return movies;
        } catch (Exception e) {
            logger.error("Fel uppstod vid hämtning av alla filmer.", e);
            throw new RuntimeException("Kunde inte hämta filmer.", e);
        }
    }

    public Movie getMovieById(Long id) {
        try {
            logger.info("Försöker hämta film med ID: {}" + id);
            Optional<Movie> movie = movieRepository.findById(id);
            if (movie.isPresent()) {
                logger.info("Film hittades: {}" + movie.get().getTitle());
                return movie.get();
            } else {
                logger.warn("Ingen film hittades med ID: {}" + id);
                return null;
            }
        } catch (Exception e) {
            logger.error("Fel uppstod vid hämtning av film med ID: {}" + id, e);
            throw new RuntimeException("Kunde inte hämta filmen.", e);
        }
    }

    public List<Movie> searchMoviesByTitle(String title) {
        try {
            logger.info("Söker efter filmer med titel: {}" + title);
            List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(title);
            logger.info("Antal filmer hittade med titel '{}': {}"+ title + movies.size());
            return movies;
        } catch (Exception e) {
            logger.error("Fel uppstod vid sökning efter filmer med titel: {}" + title, e);
            throw new RuntimeException("Kunde inte genomföra sökningen.", e);
        }
    }

    public String updateMovieOverview(Movie movie) {
        try {
            logger.info("Uppdaterar overview för filmen: {}" + movie.getTitle());
            movie.setOverview(movie.getOverview() + ", " + movie.getTitle());
            String updatedTitle = movieRepository.saveAndFlush(movie).getTitle();
            logger.info("Overview uppdaterad för filmen: {}" + updatedTitle);
            return updatedTitle;
        } catch (Exception e) {
            logger.error("Fel uppstod vid uppdatering av overview för filmen: {}" + movie.getTitle(), e);
            throw new RuntimeException("Kunde inte uppdatera overview för filmen.", e);
        }
    }
}
