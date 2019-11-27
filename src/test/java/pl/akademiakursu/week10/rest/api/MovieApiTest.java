package pl.akademiakursu.week10.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.akademiakursu.week10.rest.api.dao.entity.Movie;
import pl.akademiakursu.week10.service.MovieService;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class MovieApiTest {

    private MockMvc mockMvc;

    @InjectMocks
    private MovieApi movieApi;

    @Mock
    private MovieService movieService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieApi).build();
    }

    @Test
    public void getAllMovies() throws Exception {
        //given
        List<Movie> movies = Arrays.asList(new Movie(), new Movie());
        when(movieService.getAllMovies()).thenReturn(movies);

        //when
        mockMvc.perform(get("/movies/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    void getMovieById() throws Exception {
        //given
        Movie movie = new Movie("Name", 1990);
        when(movieService.getMovieById(anyLong())).thenReturn(java.util.Optional.of(movie));

        //when
        mockMvc.perform(get("/movies?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(movie.getName()))
                .andExpect(jsonPath("$.year").value(movie.getYear()));

        verify(movieService, times(1)).getMovieById(anyLong());
    }

    @Test
    void addMovie() throws Exception {
        //given
        Movie movie = new Movie("Name", 1999);
        when(movieService.save(ArgumentMatchers.any(Movie.class))).thenReturn(movie);

        //when
        mockMvc.perform(post("/movies")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(movie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(movie.getName()))
                .andExpect(jsonPath("$.year").value(movie.getYear()));

        verify(movieService, times(1)).save(ArgumentMatchers.any(Movie.class));
    }

    @Test
    void deleteMovie() throws Exception {
        //given
        Long movieId = Long.valueOf(43);
        String url = "/movies/?id=" + movieId;
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url)
                .contentType(APPLICATION_JSON_UTF8);

        //when
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(movieService, times(1)).deleteById(anyLong());
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return new ObjectMapper().writeValueAsBytes(object);
    }

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );
}
