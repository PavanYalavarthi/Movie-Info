package com.projects.reactivespring.controller;

import com.projects.reactivespring.domain.MovieInfo;
import com.projects.reactivespring.service.MovieInfoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class MovieInfoController {

    private final MovieInfoService movieInfoService;

    public MovieInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @PostMapping("/movieinfo")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.addMovieInfo(movieInfo);
    }

    @GetMapping("/movieinfo")
    public Flux<MovieInfo> getAllMovieInfos(@RequestParam(value = "year", required = false) Integer year) {
        if (year != null) {
            return movieInfoService.getAllMovieInfosByYear(year);
        }
        return movieInfoService.getAllMovieInfos();
    }

    @GetMapping("/movieinfo/{id}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable String id) {
        return movieInfoService.getMovieInfoById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/movieinfo/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfoById(@PathVariable String id, @RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.updateMovieInfoById(id, movieInfo)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/movieinfo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfoById(@PathVariable String id) {
        return movieInfoService.deleteMovieInfoById(id);
    }
}
