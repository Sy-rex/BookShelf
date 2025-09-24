package com.sobolev.spring.bookshelf.controller;

import com.sobolev.spring.bookshelf.dto.request.GenreRequest;
import com.sobolev.spring.bookshelf.dto.response.GenreResponse;
import com.sobolev.spring.bookshelf.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/genres")
@Tag(name = "Genre Controller", description = "Controller for managing genres")
@Slf4j
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    @Operation(
            summary = "find all genres"
    )
    public ResponseEntity<List<GenreResponse>> findAll() {
        log.info("find all genres");
        return new ResponseEntity<>(genreService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "find genre by id"
    )
    @ApiResponse(responseCode = "404", description = "Not found genre")
    public ResponseEntity<GenreResponse> findById(@PathVariable @Parameter(required = true) Long id) {
        log.info("find genre by id: {}", id);
        return genreService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @Operation(
            summary = "find genre by name of genre"
    )
    public ResponseEntity<GenreResponse> getGenreByName(@PathVariable
                                                            @Parameter(description = "name of genre", required = true) String name) {
        log.info("find genre by name: {}", name);
        return genreService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Add new genre"
    )
    public ResponseEntity<GenreResponse> createGenre(@Valid @RequestBody GenreRequest genreRequest) {
        return new ResponseEntity<>(genreService.create(genreRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "update genre by id"
    )
    @ApiResponse(responseCode = "404", description = "Not found genre")
    public ResponseEntity<GenreResponse> updateGenre(@Valid @RequestBody GenreRequest genreRequest,
                                                     @PathVariable @Parameter(required = true) Long id) {
        log.info("update genre by id: {}", id);
        return genreService.update(id, genreRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete genre by id"
    )
    @ApiResponse(responseCode = "404", description = "Not found genre")
    public ResponseEntity<Void> deleteGenre(@PathVariable @Parameter(required = true) Long id) {
        boolean success = genreService.deleteById(id);
        return new ResponseEntity<>(success ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
