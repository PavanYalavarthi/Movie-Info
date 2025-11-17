package com.projects.reactivespring.controller;

import com.projects.reactivespring.domain.MovieInfo;
import com.projects.reactivespring.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MovieInfoControllerTest {

    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @Autowired
    private WebTestClient webTestClient;

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
    void addMovieInfo() {
        var newMovieInfo = new MovieInfo(null, "Varanasi", List.of("Mahesh babu", "Priyanka Jonas"), LocalDate.parse("2027-05-28"));
        webTestClient.post()
                .uri("/v1/movieinfo")
                .bodyValue(newMovieInfo)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(response -> {
                    var body = response.getResponseBody();
                    assertNotNull(body);
                    assertNotNull(body.getId());
                });

    }

    @Test
    void getAllMovieInfos() {
        webTestClient.get()
                .uri("/v1/movieinfo")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void getMovieInfoById() {
        webTestClient.get()
                .uri("/v1/movieinfo/Epic")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Bahubali - The Epic");
    }

    @Test
    void updateMovieInfoById() {
        var epicMovie = movieInfoRepository.findById("Epic").block();
        assertNotNull(epicMovie);
        epicMovie.setReleaseDate(LocalDate.parse("2025-10-29"));

        webTestClient.post()
                .uri("/v1/movieinfo/Epic")
                .bodyValue(epicMovie)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(response -> {
                    var body = response.getResponseBody();
                    assertNotNull(body);
                    assertEquals("Bahubali - The Epic", body.getTitle());
                });
    }

    @Test
    void deleteMovieInfoById() {
        webTestClient.delete()
                .uri("/v1/movieinfo/Epic")
                .exchange()
                .expectStatus().isNoContent();
    }
}