package com.sobolev.spring.bookshelf.controller;

import com.sobolev.spring.bookshelf.dto.request.BookRequest;
import com.sobolev.spring.bookshelf.dto.response.BookResponse;
import com.sobolev.spring.bookshelf.model.BookStatus;
import com.sobolev.spring.bookshelf.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@Tag(name = "BookController", description = "Controller for managing books")
@Slf4j
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(
            summary = "Get all books",
            description = "return all books"
    )
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        log.info("Get all books");
        List<BookResponse> books = bookService.findAll();
        log.info("Found {} books", books.size());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get book by id",
            description = "Return book by id"
    )
    @ApiResponse(responseCode = "404", description = "Not found book")
    public ResponseEntity<BookResponse> getBookById(@PathVariable @Parameter(description = "Identifier of book", required = true) Long id){
        log.info("Get book by id: {}", id);
        Optional<BookResponse> book = bookService.findById(id);
        if(book.isPresent()){
            log.info("Found book: {}", book.get().getId());
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        log.info("Not found book by id: {}", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @Operation(
            summary = "Create book",
            description = "endpoint for create book"
    )
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest){
        log.info("Create book: {}", bookRequest);
        BookResponse book = bookService.create(bookRequest);
        log.info("Created book: {}", book);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update book",
            description = "endpoint for update book"
    )
    @ApiResponse(responseCode = "404", description = "Not found book")
    public ResponseEntity<BookResponse> updateBook(@PathVariable @Parameter(description = "Identifier of book", required = true) Long id,
                                                   @Valid @RequestBody BookRequest bookRequest){
        log.info("Update book: {}", bookRequest);
        Optional<BookResponse> book = bookService.update(id, bookRequest);
        if (book.isPresent()){
            log.info("Found book: {}", book.get());
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        log.info("Not found book update");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete book",
            description = "Delete book"
    )
    @ApiResponse(responseCode = "404", description = "Not found book")
    public ResponseEntity<Void> deleteBook(@PathVariable @Parameter(description = "Identifier of book", required = true) Long id){
        log.info("Delete book: {}", id);
        boolean deleted = bookService.deleteById(id);

        if (deleted){
            log.info("Deleted book: {}", id);
            return ResponseEntity.ok().build();
        }
        log.info("Not found book deleted");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/author/{author}")
    @Operation(
            summary = "Get books by author",
            description = "Return book by author"
    )
    public ResponseEntity<Page<BookResponse>> getBooksByAuthor(@PathVariable String author,
                                                               @PageableDefault(sort = "publicationYear",
                                                                       direction = Sort.Direction.DESC) Pageable pageable
                                                               ){
        Page<BookResponse> books = bookService.findByAuthor(author, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Get books by status",
            description = "Return book by status"
    )
    public ResponseEntity<List<BookResponse>> getBooksByStatus(@PathVariable BookStatus status){
        List<BookResponse> books = bookService.findByStatus(status);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/year/{year}")
    @Operation(
            summary = "Get books by publicationYear",
            description = "Return book by publicationYear"
    )
    public ResponseEntity<List<BookResponse>> getBooksByPublicationYear(@PathVariable Integer year){
        List<BookResponse> books = bookService.findByPublicationYear(year);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/year-range")
    @Operation(
            summary = "Get books by year range",
            description = "Return books where publicationYear between startYear and endYear"
    )
    public ResponseEntity<List<BookResponse>> getBooksByYearRange(
            @RequestParam Integer startYear,
            @RequestParam Integer endYear){
        List<BookResponse> books = bookService.findByPublicationYearBetween(startYear, endYear);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/genre/{genreId}")
    @Operation(
            summary = "Get books by genreId",
            description = "Return books by genreId"
    )
    public ResponseEntity<List<BookResponse>> getBooksByGenre(@PathVariable Long genreId){
        List<BookResponse> books = bookService.findBooksByGenreId(genreId);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/genre-name/{genreName}")
    @Operation(
            summary = "Get books by genreName",
            description = "Return books by genreName"
    )
    public ResponseEntity<List<BookResponse>> getBooksByGenreName(@PathVariable String genreName){
        List<BookResponse> books = bookService.findBooksByGenreName(genreName);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/popular")
    @Operation(
            summary = "Get popular books",
            description = "Return popular books sorted by rating"
    )
    public ResponseEntity<List<BookResponse>> getPopularBooks(){
        List<BookResponse> books = bookService.findPopularBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/high-rated")
    @Operation(
            summary = "Get books with higher rating",
            description = "Return books with average rating grater when minRating"
    )
    public ResponseEntity<List<BookResponse>> getHighRatedBooks(
            @RequestParam(defaultValue = "4.0") Double minRating){
        List<BookResponse> books = bookService.findBooksWithAverageRatingAbove(minRating);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/stats/author/{author}")
    @Operation(
            summary = "Get count books by author"
    )
    public ResponseEntity<Long> getBookCountByAuthor(@PathVariable String author){
        long bookCount = bookService.countByAuthor(author);
        return ResponseEntity.ok(bookCount);
    }
}
