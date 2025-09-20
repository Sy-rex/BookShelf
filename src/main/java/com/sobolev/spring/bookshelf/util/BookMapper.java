package com.sobolev.spring.bookshelf.util;

import com.sobolev.spring.bookshelf.dto.request.BookRequest;
import com.sobolev.spring.bookshelf.dto.response.BookResponse;
import com.sobolev.spring.bookshelf.dto.response.GenreResponse;
import com.sobolev.spring.bookshelf.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {

    private final GenreMapper genreMapper;

    @Autowired
    public BookMapper(GenreMapper genreMapper) {
        this.genreMapper = genreMapper;
    }

    public Book toEntity(BookRequest bookRequest) {
        if (bookRequest == null) return null;

        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setPublicationYear(bookRequest.getPublicationYear());
        book.setStatus(bookRequest.getStatus());

        return book;
    }

    public BookResponse toResponse(Book book) {
        if (book == null) return null;

        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setStatus(book.getStatus());
        response.setPublicationYear(book.getPublicationYear());

        if (book.getGenres() != null) {
            response.setGenres(
                    book.getGenres()
                            .stream()
                            .map(genreMapper::toResponse)
                            .collect(Collectors.toSet())
            );
        }

        if (book.getReviews() != null) {
            response.setReviewCount(book.getReviews().size());

            if (!book.getReviews().isEmpty()) {
                double average = book.getReviews()
                        .stream().mapToInt(review -> review.getRating() != null ? review.getRating() : 0)
                        .average()
                        .orElse(0.0);
                response.setAverageRating(Math.round(average * 10) / 10.0);
            }else {
                response.setAverageRating(0.0);
            }

        }else{
            response.setReviewCount(0);
            response.setAverageRating(0.0);
        }

        return response;
    }

    public void updateEntityFromRequest(BookRequest bookRequest, Book book) {
        if (bookRequest == null || book == null) return;

        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setPublicationYear(bookRequest.getPublicationYear());
        book.setStatus(bookRequest.getStatus());
    }
}
