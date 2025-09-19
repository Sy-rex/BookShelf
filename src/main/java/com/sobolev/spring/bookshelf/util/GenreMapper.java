package com.sobolev.spring.bookshelf.util;

import com.sobolev.spring.bookshelf.dto.request.GenreRequest;
import com.sobolev.spring.bookshelf.dto.response.GenreResponse;
import com.sobolev.spring.bookshelf.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

    public Genre toEntity(GenreRequest genreRequest) {
        if (genreRequest == null)
            return null;


        Genre genre = new Genre();
        genre.setName(genreRequest.getName());
        return genre;
    }

    public GenreResponse toResponse(Genre genre) {
        if (genre == null)
            return null;

        GenreResponse genreResponse = new GenreResponse();
        genreResponse.setId(genre.getId());
        genreResponse.setName(genre.getName());
        return genreResponse;
    }

    public void updateEntityFromRequest(GenreRequest genreRequest, Genre genre) {
        if (genreRequest == null || genre == null)
            return;

        genre.setName(genreRequest.getName());
    }
}
