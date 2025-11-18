package com.projects.reactivespring.repository;

import com.projects.reactivespring.domain.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String> {
}
