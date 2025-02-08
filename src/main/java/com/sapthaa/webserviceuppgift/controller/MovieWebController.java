package com.sapthaa.webserviceuppgift.controller;

import com.sapthaa.webserviceuppgift.model.Movie;
import com.sapthaa.webserviceuppgift.service.MovieWebService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MovieWebController {

    private final MovieWebService movieWebService;

    public MovieWebController(MovieWebService movieWebService) {
        this.movieWebService = movieWebService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("authenticated", SecurityContextHolder.getContext().getAuthentication().getName());
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/all-movies")
    public String getAllMovies(Model model) {
        List<Movie> movies = movieWebService.getAllMoviesForWeb();
        model.addAttribute("movies", movies);
        return "all-movies";
    }

    @GetMapping("/movie-detail/{id}")
    public String getMovieDetails(@PathVariable("id") Long id, Model model) {
        Movie movie = movieWebService.getMovieById(id);
        if (movie == null) {
            return "error";
        }
        model.addAttribute("movie", movie);
        return "movie-detail";
    }

    @GetMapping("/search-movie")
    public String searchMovie(@RequestParam(required = false) String title, Model model) {
        if (title == null || title.isEmpty()) {
            model.addAttribute("error", "Sökterm kan inte vara tom.");
            return "search-movie";
        }
        List<Movie> movies = movieWebService.searchMoviesByTitle(title);
        model.addAttribute("movies", movies);
        return "search-movie";
    }

    @PostMapping("/movie-detail/{id}")
    public String updatMovieDetails(@PathVariable("id") Long id, Movie movie, Model model) {
        if (movie == null) {
            return "error";
        }
        movieWebService.updateMovieOverview(movieWebService.getMovieById(id));
        model.addAttribute("movie", movie);
        return "/movie-detail";

    }

}
