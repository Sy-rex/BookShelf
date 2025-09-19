package com.sobolev.spring.bookshelf.util;

import com.sobolev.spring.bookshelf.dto.request.ReviewRequest;
import com.sobolev.spring.bookshelf.dto.response.ReviewResponse;
import com.sobolev.spring.bookshelf.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final BookMapper bookMapper;

    @Autowired
    public ReviewMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public Review toEntity(ReviewRequest reviewRequest) {
        if (reviewRequest == null)
            return null;

        Review review = new Review();
        review.setRating(reviewRequest.getRating());
        review.setContent(reviewRequest.getContent());
        return review;
    }

    public ReviewResponse toResponse(Review review) {
        if (review == null)
            return null;

        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setRating(review.getRating());
        reviewResponse.setContent(review.getContent());
        reviewResponse.setId(review.getId());
        reviewResponse.setCreatedAt(review.getCreatedAt());
        reviewResponse.setBook(bookMapper.toResponse(review.getBook()));

        return reviewResponse;
    }

    public void updateEntityFromRequest(ReviewRequest reviewRequest, Review review) {
        if (reviewRequest == null || review == null)
            return;

        review.setRating(reviewRequest.getRating());
        review.setContent(reviewRequest.getContent());
    }
}
