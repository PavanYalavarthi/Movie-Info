package com.projects.reactivespring.repository;

import com.projects.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryTest {

    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        List<MovieInfo> movieInfos = List.of(
                new MovieInfo(null, "Bahubali - The Beginning", List.of("Prabhas", "Anushka", "Rana"), LocalDate.parse("2015-07-10")),
                new MovieInfo(null, "Bahubali - The Conclusion", List.of("Prabhas", "Anushka", "Rana"), LocalDate.parse("2017-04-28")),
                new MovieInfo("Epic", "Bahubali - The Epic", List.of("Prabhas", "Anushka", "Rana"), LocalDate.parse("2025-10-31"))
        );
        movieInfoRepository.saveAll(movieInfos).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        var result = movieInfoRepository.findAll();

        StepVerifier.create(result)
                .expectNextCount(3)
                .verifyComplete();

    }

    @Test
    void findById() {
        var result = movieInfoRepository.findById("Epic");

        StepVerifier.create(result)
                .assertNext(movieInfo -> {
                    assertEquals("Bahubali - The Epic", movieInfo.getTitle());
                })
                .verifyComplete();

    }

    @Test
    void saveMovieInfo() {
        var newMovieInfo = new MovieInfo(null, "Varanasi", List.of("Mahesh babu", "Priyanka Jonas"), LocalDate.parse("2027-05-28"));
        var result = movieInfoRepository.save(newMovieInfo);

        StepVerifier.create(result)
                .assertNext(movieInfo -> {
                    assertNotNull(movieInfo.getId());
                    assertEquals("Varanasi", movieInfo.getTitle());
                })
                .verifyComplete();

    }

    @Test
    void updateMovieInfo() {
        var epicMovie = movieInfoRepository.findById("Epic").block();
        assertNotNull(epicMovie);
        epicMovie.setReleaseDate(LocalDate.parse("2025-10-29"));
        var result = movieInfoRepository.save(epicMovie);

        StepVerifier.create(result)
                .assertNext(movieInfo -> {
                    assertEquals(2025, movieInfo.getReleaseDate().getYear());
                })
                .verifyComplete();

    }

    @Test
    void deleteMovieInfo() {
        movieInfoRepository.deleteById("Epic").block();
        var result = movieInfoRepository.findAll();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();

    }
}