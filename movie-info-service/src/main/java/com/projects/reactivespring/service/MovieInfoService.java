package com.projects.reactivespring.service;

import com.projects.reactivespring.domain.MovieInfo;
import com.projects.reactivespring.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

    private final MovieInfoRepository movieInfoRepository;

    public MovieInfoService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo);
    }

    public Flux<MovieInfo> getAllMovieInfos() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getMovieInfoById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<MovieInfo> updateMovieInfoById(String id, MovieInfo updatedMovieInfo) {
        return movieInfoRepository.findById(id)
                .flatMap(movieInfo ->  {
                    movieInfo.setTitle(movieInfo.getTitle());
                    movieInfo.setReleaseDate(movieInfo.getReleaseDate());
                    movieInfo.setCast(movieInfo.getCast());
                    return movieInfoRepository.save(movieInfo);
                });
    }

    public Mono<Void> deleteMovieInfoById(String id) {
        return movieInfoRepository.deleteById(id);
    }
}
