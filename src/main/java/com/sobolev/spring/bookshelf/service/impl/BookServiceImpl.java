package com.sobolev.spring.bookshelf.service.impl;

import com.sobolev.spring.bookshelf.dto.request.BookRequest;
import com.sobolev.spring.bookshelf.dto.response.BookResponse;
import com.sobolev.spring.bookshelf.exception.ResourseNotFoundException;
import com.sobolev.spring.bookshelf.model.Book;
import com.sobolev.spring.bookshelf.model.Genre;
import com.sobolev.spring.bookshelf.repository.BookRepository;
import com.sobolev.spring.bookshelf.repository.GenreRepository;
import com.sobolev.spring.bookshelf.service.BookService;
import com.sobolev.spring.bookshelf.service.GenreService;
import com.sobolev.spring.bookshelf.util.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final GenreRepository genreRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository,
                           BookMapper bookMapper, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookResponse> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toResponse);
    }

    @Override
    public BookResponse create(BookRequest bookRequest) {
        Book book = bookMapper.toEntity(bookRequest);

        if (bookRequest.getGenreIds() != null && !bookRequest.getGenreIds().isEmpty()) {
            long existingGenresCount = genreRepository.countByIdIn(bookRequest.getGenreIds());
            if (existingGenresCount != bookRequest.getGenreIds().size()) {
                throw new ResourseNotFoundException("One or more genres not found");
            }

            Set<Genre> genreProxies = bookRequest.getGenreIds()
                    .stream()
                    .map(genreRepository::getReferenceById)
                    .collect(Collectors.toSet());

            book.getGenres().clear();
            book.getGenres().addAll(genreProxies);
        }

        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }

    @Override
    public Optional<BookResponse> update(Long id, BookRequest bookRequest) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    bookMapper.updateEntityFromRequest(bookRequest, existingBook);

                    if (bookRequest.getGenreIds() != null) {
                        long existingGenresCount = genreRepository.countByIdIn(bookRequest.getGenreIds());

                        if (existingGenresCount != bookRequest.getGenreIds().size()) {
                            throw new ResourseNotFoundException("One or more genres not found");
                        }

                        Set<Genre> genreProxies = bookRequest.getGenreIds()
                                .stream()
                                .map(genreRepository::getReferenceById)
                                .collect(Collectors.toSet());

                        existingBook.getGenres().clear();
                        existingBook.getGenres().addAll(genreProxies);
                    }

                    Book updatedBook = bookRepository.save(existingBook);
                    return bookMapper.toResponse(updatedBook);
                });
    }

    @Override
    public boolean deleteById(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
