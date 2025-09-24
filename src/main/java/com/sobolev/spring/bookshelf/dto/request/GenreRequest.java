package com.sobolev.spring.bookshelf.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "genre dto")
public class GenreRequest {

    @NotBlank(message = "Genre name is required")
    @Schema(description = "name is required")
    private String name;
}
