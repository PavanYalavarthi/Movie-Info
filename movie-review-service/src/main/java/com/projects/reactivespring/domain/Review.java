package com.projects.reactivespring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Review {
    @Id
    private String reviewId;
    private String movieInfoId;
    private String author;
    private String comment;
    private Double rating;
}
