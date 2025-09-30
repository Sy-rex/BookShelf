package com.sobolev.spring.bookshelf.repository;

import com.sobolev.spring.bookshelf.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);

    List<Review> findByRating(Integer rating);

    List<Review> findByRatingGreaterThan(Integer rating);

    List<Review> findByRatingLessThan(Integer rating);

    List<Review> findByRatingBetween(Integer minRating, Integer maxRating);

    List<Review> findByBookIdOrderByRatingDesc(Long bookId);

    List<Review> findByBookIdOrderByCreatedAtDesc(Long bookId);

    long countByBookId(Long bookId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double findAverageRatingByBookId(@Param("bookId") Long bookId);
}
