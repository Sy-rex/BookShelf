package com.sobolev.spring.bookshelf.dto.request;

import com.sobolev.spring.bookshelf.model.BookStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class BookRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author must be less than 255 characters")
    private String author;

    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
    private String isbn;

    @Min(value = 1, message = "Publication year must be at least 1")
    private Integer publicationYear;

    @NotNull(message = "message is required")
    private BookStatus status;

    private Set<Long> genreIds;
}
