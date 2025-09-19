package com.sobolev.spring.bookshelf.service;

import com.sobolev.spring.bookshelf.dto.request.BookRequest;
import com.sobolev.spring.bookshelf.dto.response.BookResponse;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookResponse> findAll();
    Optional<BookResponse> findById(Long id);
    BookResponse create(BookRequest bookRequest);
    BookResponse update(Long id, BookRequest bookRequest);
    void deleteById(Long id);
}
