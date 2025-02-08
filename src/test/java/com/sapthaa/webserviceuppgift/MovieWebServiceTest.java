package com.sapthaa.webserviceuppgift;

import com.sapthaa.webserviceuppgift.model.Movie;
import com.sapthaa.webserviceuppgift.movieRepository.MovieRepository;
import com.sapthaa.webserviceuppgift.service.LoggerService;
import com.sapthaa.webserviceuppgift.service.MovieWebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieWebServiceTest {

    @Mock
    private LoggerService logger;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieWebService movieWebService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMoviesForWeb() {

        Movie movie1 = new Movie();
        movie1.setTitle("The Hobbit");
        movie1.setMovieId(2);
        movie1.setOverview("The Hobbits and the wild");
        Movie movie2 = new Movie();
        movie2.setTitle("The Habbit");
        movie2.setMovieId(3);
        movie2.setOverview("The Hobbits and the wild");
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));

        List<Movie> movies = movieWebService.getAllMoviesForWeb();

        assertEquals("The Hobbits and the wild", movie1.getOverview());
        assertNotNull(movies);
        assertEquals(2, movies.size());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testGetMovieById() {

        Long movieId = 1L;
        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setTitle("Movie 1");
        movie.setOverview("The Hobbits and the wild");
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        Movie result = movieWebService.getMovieById(movieId);

        assertNotNull(result);
        assertEquals("Movie 1", result.getTitle());
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void testSearchMoviesByTitle() {

        Movie movie1 = new Movie();
        movie1.setTitle("Movie 1");
        movie1.setMovieId(2);
        movie1.setOverview("The Hobbits and the wild");
        Movie movie2 = new Movie();
        movie2.setTitle("Movie 2");
        movie2.setMovieId(3);
        movie2.setOverview("The Hobbits and the wild");
        when(movieRepository.findByTitleContainingIgnoreCase(movie1.getTitle())).thenReturn(List.of(movie1, movie2));


        List<Movie> movies = movieWebService.searchMoviesByTitle(movie1.getTitle());

        assertNotNull(movies);
        assertEquals(2, movies.size());
        assertTrue(movies.stream().allMatch(movie -> movie.getTitle().contains("Movie")));
        verify(movieRepository, times(1)).findByTitleContainingIgnoreCase(movie1.getTitle());
    }

    @Test
    void testUpdateMovieOverview() {

        Movie movie1 = new Movie();
        movie1.setTitle("Movie 1");
        movie1.setMovieId(2);
        movie1.setOverview("Overview 1");
        String newOverview = "Overview 1, Movie 1";
        when(movieRepository.saveAndFlush(movie1)).thenReturn(movie1);

        String updatedTitle = movieWebService.updateMovieOverview(movie1);

        assertEquals("Movie 1", updatedTitle);
        assertEquals(newOverview, movie1.getOverview());
        verify(movieRepository, times(1)).saveAndFlush(movie1);
    }
}
