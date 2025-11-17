package com.projects.reactivespring.controller;

import com.projects.reactivespring.domain.MovieInfo;
import com.projects.reactivespring.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebFluxTest(controllers = MovieInfoController.class)
@AutoConfigureWebTestClient
class MovieInfoControllerUnitTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private MovieInfoService movieInfoService;

    @Test
    void getAllMoviesInfo() {
        List<MovieInfo> movieInfos = List.of(
                new MovieInfo(null, "Bahubali - The Beginning", List.of("Prabhas", "Anushka", "Rana"), LocalDate.parse("2015-07-10")),
                new MovieInfo(null, "Bahubali - The Conclusion", List.of("Prabhas", "Anushka", "Rana"), LocalDate.parse("2017-04-28")),
                new MovieInfo("Epic", "Bahubali - The Epic", List.of("Prabhas", "Anushka", "Rana"), LocalDate.parse("2025-10-31"))
        );

        when(movieInfoService.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfos));

        webTestClient.get()
                .uri("/v1/movieinfo")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void addMovieInfo() {
        var newMovieInfo = new MovieInfo(null, "Varanasi", List.of("Mahesh babu", "Priyanka Jonas"), LocalDate.parse("2027-05-28"));
        when(movieInfoService
                .addMovieInfo(isA(MovieInfo.class)))
                .thenReturn(Mono.just(
                        new MovieInfo("mockId", "Varanasi", List.of("Mahesh babu", "Priyanka Jonas"), LocalDate.parse("2027-05-28"))
                ));

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
                    assertEquals("mockId", body.getId());
                });

    }

    @Test
    void updateMovieInfoById() {
        var newMovieInfo = new MovieInfo(null, "Varanasi", List.of("Mahesh babu", "Priyanka Jonas"), LocalDate.parse("2027-05-28"));

        when(movieInfoService
                .updateMovieInfoById(isA(String.class), isA(MovieInfo.class)))
                .thenReturn(Mono.just(
                        new MovieInfo("mockId", "Varanasi", List.of("Mahesh babu", "Priyanka Jonas"), LocalDate.parse("2027-05-28"))
                ));

        webTestClient.post()
                .uri("/v1/movieinfo/Epic")
                .bodyValue(newMovieInfo)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(response -> {
                    var body = response.getResponseBody();
                    assertNotNull(body);
                    assertEquals("Varanasi", body.getTitle());
                    assertEquals("mockId", body.getId());
                });
    }

    @Test
    void addMovieInfo_Validation() {
        var newMovieInfo = new MovieInfo(null, "", List.of("Mahesh babu", "Priyanka Jonas"), LocalDate.parse("2027-05-28"));
        when(movieInfoService
                .addMovieInfo(isA(MovieInfo.class)))
                .thenReturn(Mono.just(
                        new MovieInfo("mockId", "Varanasi", List.of("Mahesh babu", "Priyanka Jonas"), LocalDate.parse("2027-05-28"))
                ));

        webTestClient.post()
                .uri("/v1/movieinfo")
                .bodyValue(newMovieInfo)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(response -> {
                    var body = response.getResponseBody();
                    assertEquals("Title needed to be present", body);
                });

    }
}