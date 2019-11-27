package pl.akademiakursu.week10.rest.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.akademiakursu.week10.aspect.AddMovie;
import pl.akademiakursu.week10.rest.api.dao.entity.Movie;
import pl.akademiakursu.week10.service.MovieService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieApi {

    private MovieService movieService;

    @Autowired
    public MovieApi(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping
    public Movie getMovieById(@RequestParam Long id) {
        Optional<Movie> movie = movieService.getMovieById(id);

        if (movie.isPresent()) {
            return movie.get();
        }

        return null;
    }

    @PostMapping
    public Movie addMovie(@RequestBody Movie movie) {
        return movieService.save(movie);
    }

    @DeleteMapping
    public void deleteMovie(@RequestParam Long id) {
        movieService.deleteById(id);
    }
}
