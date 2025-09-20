package com.sobolev.spring.bookshelf.service.impl;

import com.sobolev.spring.bookshelf.dto.request.ReviewRequest;
import com.sobolev.spring.bookshelf.dto.response.BookResponse;
import com.sobolev.spring.bookshelf.dto.response.ReviewResponse;
import com.sobolev.spring.bookshelf.model.Book;
import com.sobolev.spring.bookshelf.model.Review;
import com.sobolev.spring.bookshelf.repository.ReviewRepository;
import com.sobolev.spring.bookshelf.service.BookService;
import com.sobolev.spring.bookshelf.service.ReviewService;
import com.sobolev.spring.bookshelf.util.BookMapper;
import com.sobolev.spring.bookshelf.util.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookService bookService;
    private final ReviewMapper reviewMapper;
    private final BookMapper bookMapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ReviewMapper reviewMapper,
                             BookService bookService, BookMapper bookMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> findAll() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewResponse> findById(Long id) {
        return reviewRepository.findById(id).map(reviewMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> findByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId)
                .stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse create(ReviewRequest reviewRequest) {
        Optional<BookResponse> bookResponse = bookService.findById(reviewRequest.getBookId());
        if (bookResponse.isEmpty()) {
            throw new RuntimeException("Book not found with id " + reviewRequest.getBookId());
        }

        Review review = reviewMapper.toEntity(reviewRequest);

        Book book = new Book();
        book.setId(bookResponse.get().getId());
        review.setBook(book);

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toResponse(savedReview);
    }

    @Override
    public Optional<ReviewResponse> update(Long id, ReviewRequest reviewRequest) {
        return reviewRepository.findById(id)
                .map(existingReview -> {
                    reviewMapper.updateEntityFromRequest(reviewRequest, existingReview);

                    if (!existingReview.getBook().getId().equals(reviewRequest.getBookId())) {
                        Optional<BookResponse> bookResponse = bookService.findById(reviewRequest.getBookId());

                        if (bookResponse.isEmpty()) {
                            throw new RuntimeException("Book not found with id " + reviewRequest.getBookId());
                        }

                        Book book = new Book();
                        book.setId(reviewRequest.getBookId());
                        existingReview.setBook(book);
                    }

                    Review updatedReview = reviewRepository.save(existingReview);
                    return reviewMapper.toResponse(updatedReview);
                });
    }

    @Override
    public boolean deleteById(Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
