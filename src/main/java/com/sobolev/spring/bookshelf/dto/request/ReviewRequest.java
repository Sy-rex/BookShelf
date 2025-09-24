package com.sobolev.spring.bookshelf.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "review dto")
public class ReviewRequest {
    @NotNull(message = "Book ID is required")
    @Schema(description = "Type Long")
    private Long bookId;

    private String content;

    @NotNull
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
}
