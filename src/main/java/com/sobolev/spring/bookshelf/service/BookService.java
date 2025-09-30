package com.sobolev.spring.bookshelf.service;

import com.sobolev.spring.bookshelf.dto.request.BookRequest;
import com.sobolev.spring.bookshelf.dto.response.BookResponse;
import com.sobolev.spring.bookshelf.model.BookStatus;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookResponse> findAll();
    Optional<BookResponse> findById(Long id);
    BookResponse create(BookRequest bookRequest);
    Optional<BookResponse> update(Long id, BookRequest bookRequest);
    boolean deleteById(Long id);
    List<BookResponse> findByAuthor(String author);
    List<BookResponse> findByStatus(BookStatus status);
    List<BookResponse> findByPublicationYear(Integer publicationYear);
    List<BookResponse> findByPublicationYearBetween(Integer startYear, Integer endYear);
    List<BookResponse> findBooksByGenreId(Long genreId);
    List<BookResponse> findBooksByGenreName(String name);
    List<BookResponse> findPopularBooks();
    List<BookResponse> findBooksWithAverageRatingAbove(Double minRating);
    long countByAuthor(String author);
}
