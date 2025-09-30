package com.sobolev.spring.bookshelf.service.impl;

import com.sobolev.spring.bookshelf.dto.request.BookRequest;
import com.sobolev.spring.bookshelf.dto.response.BookResponse;
import com.sobolev.spring.bookshelf.exception.ResourseNotFoundException;
import com.sobolev.spring.bookshelf.model.Book;
import com.sobolev.spring.bookshelf.model.BookStatus;
import com.sobolev.spring.bookshelf.model.Genre;
import com.sobolev.spring.bookshelf.repository.BookRepository;
import com.sobolev.spring.bookshelf.repository.GenreRepository;
import com.sobolev.spring.bookshelf.service.BookService;
import com.sobolev.spring.bookshelf.util.BookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
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
        log.info("Start findAll in service");
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookResponse> findById(Long id) {
        log.info("Start findById in service");
        return bookRepository.findById(id).map(bookMapper::toResponse);
    }

    @Override
    public BookResponse create(BookRequest bookRequest) {
        log.info("Start create in service");
        Book book = bookMapper.toEntity(bookRequest);
        log.info("successful mapping to book in create");
        if (bookRequest.getGenreIds() != null && !bookRequest.getGenreIds().isEmpty()) {
            long existingGenresCount = genreRepository.countByIdIn(bookRequest.getGenreIds());
            log.info("existingGenresCount: {}", existingGenresCount);
            if (existingGenresCount != bookRequest.getGenreIds().size()) {
                throw new ResourseNotFoundException("One or more genres not found");
            }

            Set<Genre> genreProxies = bookRequest.getGenreIds()
                    .stream()
                    .map(genreRepository::getReferenceById)
                    .collect(Collectors.toSet());

            log.info("genreProxies: {}", genreProxies);

            book.getGenres().clear();
            book.getGenres().addAll(genreProxies);
            log.info("update genres for book");
        }
        log.info("save book");
        Book savedBook = bookRepository.save(book);

        return bookMapper.toResponse(savedBook);
    }

    @Override
    public Optional<BookResponse> update(Long id, BookRequest bookRequest) {
        log.info("Start update in service");
        return bookRepository.findById(id)
                .map(existingBook -> {
                    bookMapper.updateEntityFromRequest(bookRequest, existingBook);
                    log.info("successful mapping to book in update");
                    if (bookRequest.getGenreIds() != null) {
                        long existingGenresCount = genreRepository.countByIdIn(bookRequest.getGenreIds());

                        if (existingGenresCount != bookRequest.getGenreIds().size()) {
                            throw new ResourseNotFoundException("One or more genres not found");
                        }

                        Set<Genre> genreProxies = bookRequest.getGenreIds()
                                .stream()
                                .map(genreRepository::getReferenceById)
                                .collect(Collectors.toSet());
                        log.info("genreProxies update: {}", genreProxies);
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
            log.info("delete book by id");
            bookRepository.deleteById(id);
            return true;
        }
        log.info("not found book by id");
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findByAuthor(String author) {
        log.info("Start findByAuthor in service");
        return bookRepository.findByAuthor(author)
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findByStatus(BookStatus status) {
        log.info("Start findByStatus in service");
        return bookRepository.findByStatus(status)
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findByPublicationYear(Integer publicationYear) {
        log.info("Start findByPublicationYear in service");
        return bookRepository.findByPublicationYear(publicationYear)
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findByPublicationYearBetween(Integer startYear, Integer endYear) {
        log.info("Start findByPublicationYearBetween in service");
        return bookRepository.findByPublicationYearBetween(startYear, endYear)
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findBooksByGenreId(Long genreId) {
        log.info("Start findByGenreId in service");
        return bookRepository.findBooksByGenreId(genreId)
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findBooksByGenreName(String name) {
        log.info("Start findByGenreName in service");
        return bookRepository.findBooksByGenreName(name)
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findPopularBooks() {
        log.info("Start findByPopularBooks in service");
        return bookRepository.findPopularBooks()
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findBooksWithAverageRatingAbove(Double minRating) {
        log.info("Start findByAverageRatingAbove in service");
        return bookRepository.findBooksWithAverageRatingAbove(minRating)
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByAuthor(String author) {
        return bookRepository.countByAuthor(author);
    }
}
