package pl.akademiakursu.week10.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.akademiakursu.week10.rest.api.dao.MovieRepo;
import pl.akademiakursu.week10.rest.api.dao.entity.Movie;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private MovieRepo movieRepo;

    @Autowired
    public MovieService(MovieRepo movieRepo) {
        this.movieRepo = movieRepo;
    }

    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepo.findById(id);
    }

    public Movie save(Movie movie) {
        return movieRepo.save(movie);
    }

    public void deleteById(Long id) {
        movieRepo.deleteById(id);
    }
}
