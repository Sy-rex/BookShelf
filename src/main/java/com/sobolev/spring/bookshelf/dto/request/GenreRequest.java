package com.sobolev.spring.bookshelf.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreRequest {

    @NotBlank(message = "Genre name is required")
    private String name;
}
