package com.sobolev.spring.bookshelf.service;

import com.sobolev.spring.bookshelf.dto.request.ReviewRequest;
import com.sobolev.spring.bookshelf.dto.response.ReviewResponse;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<ReviewResponse> findAll();
    Optional<ReviewResponse> findById(Long id);
    List<ReviewResponse> findByBookId(Long bookId);
    ReviewResponse create(ReviewRequest reviewRequest);
    Optional<ReviewResponse> update(Long id, ReviewRequest reviewRequest);
    boolean deleteById(Long id);
}
