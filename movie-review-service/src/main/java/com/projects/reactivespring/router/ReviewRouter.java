package com.projects.reactivespring.router;

import com.projects.reactivespring.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter {
    @Bean
    public RouterFunction<ServerResponse> reviewRoutes(ReviewHandler reviewHandler) {
        return route()
                .POST("v1/reviews", reviewHandler::addReview)
                .GET("/v1/reviews", reviewHandler::getReviews)
                .build();
    }
}
