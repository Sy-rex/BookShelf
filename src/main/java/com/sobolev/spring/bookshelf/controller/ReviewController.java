package com.sobolev.spring.bookshelf.controller;

import com.sobolev.spring.bookshelf.dto.request.ReviewRequest;
import com.sobolev.spring.bookshelf.dto.response.ReviewResponse;
import com.sobolev.spring.bookshelf.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review controller", description = "controller for review of books")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @Operation(
            summary = "get list of all reviews"
    )
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        log.info("get list of all reviews");
        return new ResponseEntity<>(reviewService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "get Review by id"
    )
    @ApiResponse(responseCode = "404", description = "Not found review")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable("id") Long id) {
        log.info("get review by id: {}", id);
        return reviewService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/book/{bookId}")
    @Operation(
            summary = "get all Reviews by book id"
    )
    @ApiResponse(responseCode = "404", description = "Not found review or book")
    public ResponseEntity<List<ReviewResponse>> getAllReviewsByBookId(@PathVariable("bookId") Long bookId) {
        log.info("get all Reviews by book id: {}", bookId);
        List<ReviewResponse> reviews = reviewService.findByBookId(bookId);
        log.info("size reviews: {}", reviews.size());
        if (reviews.isEmpty()) {
            log.info("reviews is empty");
            return ResponseEntity.noContent().build();
        }
        log.info("reviews size: {}", reviews.size());
        return ResponseEntity.ok(reviews);
    }

    @PostMapping()
    @Operation(
            summary = "create review"
    )
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        log.info("create review: {}", reviewRequest);
        return new ResponseEntity<>(reviewService.create(reviewRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "update review"
    )
    @ApiResponse(responseCode = "404", description = "Not found review")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable("id") Long id, @Valid @RequestBody ReviewRequest reviewRequest) {
        log.info("update review: {}", reviewRequest);
        return reviewService.update(id, reviewRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "delete review"
    )
    @ApiResponse(responseCode = "404", description = "Not found review")
    public ResponseEntity<Void> deleteReview(@PathVariable("id") Long id) {
        log.info("Start delete review: {}", id);
        boolean deleted = reviewService.deleteById(id);

        if (deleted){
            log.info("Deleted review: {}", id);
            return ResponseEntity.noContent().build();
        }
        log.info("Not delete review: {}", id);
        return ResponseEntity.notFound().build();
    }
}
