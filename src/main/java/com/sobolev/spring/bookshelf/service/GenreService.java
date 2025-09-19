package com.sobolev.spring.bookshelf.service;

import com.sobolev.spring.bookshelf.dto.request.GenreRequest;
import com.sobolev.spring.bookshelf.dto.response.GenreResponse;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<GenreResponse> findAll();
    GenreResponse findById(Long id);
    GenreResponse findByName(String name);
    GenreResponse create(GenreRequest genreRequest);
    GenreResponse update(Long id, GenreRequest genreRequest);
    boolean deleteById(Long id);
}
