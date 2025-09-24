package com.sobolev.spring.bookshelf.service;

import com.sobolev.spring.bookshelf.dto.request.GenreRequest;
import com.sobolev.spring.bookshelf.dto.response.GenreResponse;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<GenreResponse> findAll();
    Optional<GenreResponse> findById(Long id);
    Optional<GenreResponse> findByName(String name);
    GenreResponse create(GenreRequest genreRequest);
    Optional<GenreResponse> update(Long id, GenreRequest genreRequest);
    boolean deleteById(Long id);
}
