package com.sapthaa.webserviceuppgift.service;
import com.sapthaa.webserviceuppgift.model.Movie;
import com.sapthaa.webserviceuppgift.movieRepository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final WebClient movieWebClient;
    private final MovieRepository movieRepository;

    @Value("${api.key}")
    private String apiKey;

    public MovieService(WebClient.Builder movieWebClient, MovieRepository movieRepository) {
        this.movieWebClient = movieWebClient.baseUrl("https://api.themoviedb.org/3/movie/").build();
        this.movieRepository = movieRepository;
    }

    // Använder <Object> för att kunna returnera felmeddelanden eftersom jag inte har en error response klass

    // Get
    public ResponseEntity<Object> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();

        if (movies.isEmpty()) {
            return new ResponseEntity<>("Could not find any movies", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    // Get
    public ResponseEntity<Object> getMovieById(Long id) {
        try {
            Movie movie = movieWebClient.get()
                    .uri("/{id}?api_key={apiKey}", id, apiKey)
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();

            return new ResponseEntity<>(movie, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("Could not find movie with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    // Overview (min databas)
    public ResponseEntity<String> getMovieOverviewById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);

        if (movie.isPresent()) {
            return new ResponseEntity<>("Movie title: " + movie.get().getTitle() + " - " + movie.get().getOverview(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Movie with id " + id + " could not be found",HttpStatus.NOT_FOUND);
    }

    // Release date (min databas)
    public ResponseEntity<String> getMovieReleaseDateById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);


        if (movie.isPresent()) {
            return new ResponseEntity<>("Movie : " + movie.get().getTitle() + ", Release date: " + movie.get().getRelease_date(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Movie with id " + id + " could not be found",HttpStatus.NOT_FOUND);
    }

    // Search movie (min databas)
    public ResponseEntity<Object> searchMovieByTitle(String title) {
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(title);


        if (movies.isEmpty()) {
            return new ResponseEntity<>("Movie with title: " + title + " could not be found", HttpStatus.NOT_FOUND);
        }

        Movie movie = movies.get(0);
        return new ResponseEntity<>("Movie: " + movie.getTitle(), HttpStatus.OK);
    }

    // Post
    public ResponseEntity<Object> getAndSaveMovieById(Long id) {
        try {
            Movie movie = movieWebClient.get()
                    .uri("/{id}?api_key={apiKey}", id, apiKey)
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();

            Movie savedMovie = movieRepository.save(movie);
            return new ResponseEntity<>("Movie successfully saved: " + savedMovie.getTitle(), HttpStatus.CREATED);

        }catch (Exception e){
            return new ResponseEntity<>("Could not find or save movie with id: " + id, HttpStatus.NOT_FOUND);
        }

    }

    // Put
    public ResponseEntity<Object> updateMovieById(Long id, Movie movie){
        Optional<Movie> findMovie = movieRepository.findById(id);

        if(findMovie.isPresent()) {
            Movie updateMovie = findMovie.get();
            updateMovie.setTitle(movie.getTitle());
            updateMovie.setOverview(movie.getOverview());

            movieRepository.save(updateMovie);
            return new ResponseEntity<>("Updated movie with id: " + id + " updated", HttpStatus.OK);

        }

        return new ResponseEntity<>("Movie with id " + id + " could not be found", HttpStatus.NOT_FOUND);

    }

    // Delete
    public ResponseEntity<String> deleteMovieById(Long id) {
        if(movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return new ResponseEntity<>("Movie with id " + id + " deleted", HttpStatus.OK);
        }

        return new ResponseEntity<>("Movie with id " + id + " could not be found",HttpStatus.NOT_FOUND);

    }


}
