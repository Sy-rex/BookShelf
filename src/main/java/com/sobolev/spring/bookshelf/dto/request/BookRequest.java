package com.sobolev.spring.bookshelf.dto.request;

import com.sobolev.spring.bookshelf.model.BookStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Schema(description = "Book dto")
public class BookRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    @Schema(description = "title of book", example = "Ironman")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author must be less than 255 characters")
    private String author;

    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
    @Schema(description = "unique number of ISBN")
    private String isbn;

    @Min(value = 1, message = "Publication year must be at least 1")
    private Integer publicationYear;

    @NotNull(message = "message is required")
    @Schema(description = "status of book in shelf", allowableValues = {"AVAILABLE", "READ", "IN_PROGRESS"})
    private BookStatus status;

    @Schema(description = "set of genre id`s")
    private Set<Long> genreIds;
}
