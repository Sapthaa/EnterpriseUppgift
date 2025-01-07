package com.sapthaa.webserviceuppgift.controller;
import com.sapthaa.webserviceuppgift.model.Movie;
import com.sapthaa.webserviceuppgift.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;

    }

    // Hämtar alla filmer i min databas
    @GetMapping()
    public ResponseEntity<Object> getAllMovies() {
        return movieService.getAllMovies();
    }

    // Hämtar en film via id
    @GetMapping("/movie/{id}")
    public ResponseEntity<Object> getMovieById(@PathVariable("id") Long id) {
        return movieService.getMovieById(id);
    }

    // Hämtar en films filmbeskrivning i min databas via id
    @GetMapping("/movie/overview/{id}")
    public ResponseEntity<String> getMovieOverview(@PathVariable("id") Long id) {
        return movieService.getMovieOverviewById(id);
    }

    // Hämtar en films release date i min databas via id
    @GetMapping("/movie/release_date/{id}")
    public ResponseEntity<String> getMovieReleaseDate(@PathVariable("id") Long id) {
        return movieService.getMovieReleaseDateById(id);
    }

    // Hämtar en film i min databas via titel
    @GetMapping("/movie/search/{title}")
    public ResponseEntity<Object> searchMovieByTitle(@PathVariable("title") String title) {
        return movieService.searchMovieByTitle(title);
    }

    // Sparar en film till min databas via id
    @PostMapping("/movie/save/{id}")
    public ResponseEntity<Object> saveMovie(@PathVariable("id") Long id) {
        return movieService.getAndSaveMovieById(id);
    }

    // Uppdaterar en films titel och beskrivning via id
    @PutMapping("/movie/update/{id}")
    public ResponseEntity<Object> updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie) {
        return movieService.updateMovieById(id, movie);
    }

    // Ta bort en film via id
    @DeleteMapping("/movie/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable("id") Long id) {
        return movieService.deleteMovieById(id);
    }


}

