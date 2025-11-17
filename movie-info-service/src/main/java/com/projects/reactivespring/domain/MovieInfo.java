package com.projects.reactivespring.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class MovieInfo {
    @Id
    private String id;
    @NotBlank(message = "Title needed to be present")
    private String title;
    private List<@NotBlank(message = "Cast can't be blank") String> cast;
    private LocalDate releaseDate;
}
