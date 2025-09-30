package com.sobolev.spring.bookshelf.repository;

import com.sobolev.spring.bookshelf.model.Book;
import com.sobolev.spring.bookshelf.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);

    List<Book> findByAuthorIgnoreCase(String author);

    List<Book> findByAuthorContaining(String author);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByStatus(BookStatus status);

    List<Book> findByPublicationYear(Integer year);

    List<Book> findByPublicationYearAfter(Integer year);

    List<Book> findByPublicationYearBefore(Integer year);

    List<Book> findByPublicationYearBetween(Integer startYear, Integer endYear);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByStatusAndAuthor(BookStatus status, String author);

    List<Book> findByStatusOrAuthor(BookStatus status, String author);

    List<Book> findByTitleStartingWith(String prefix);

    List<Book> findByTitleEndingWith(String suffix);

    long countByAuthor(String author);

    long countByStatus(BookStatus status);

    void deleteByAuthor(String author);

    boolean existsByIsbn(String isbn);

    List<Book> findByAuthorOrderByTitleAsc(String author);

    List<Book> findByAuthorOrderByPublicationYearDesc(String author);

    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.id = :genreId")
    List<Book> findBooksByGenreId(@Param("genreId") Long genreId);

    @Query("SELECT b FROM Book b join b.genres g WHERE g.name = :genreName")
    List<Book> findBooksByGenreName(@Param("genreName") String genreName);

    @Query(value = "SELECT b FROM Book b " +
            "WHERE b.publiction_year BETWEEN :startYear AND :endYear " +
            "ORDER BY b.publication_year DESC"
            ,nativeQuery = true)
    List<Book> findBooksByPublicationYearRange(
            @Param("startYear") Integer startYear,
            @Param("endYear") Integer endYear);

    @Query("SELECT b, COUNT(r) as reviewCount FROM Book b LEFT JOIN b.reviews r GROUP BY b")
    List<Object[]> findBooksWithReviewCount();

    @Query("SELECT b FROM Book b LEFT JOIN b.reviews r GROUP BY b ORDER BY COUNT(r) DESC")
    List<Book> findPopularBooks();

    @Query("SELECT b FROM Book b WHERE (SELECT AVG(r.rating) FROM b.reviews r) > :minRating")
    List<Book> findBooksWithAverageRatingAbove(@Param("minRating") Double minRating);
}
