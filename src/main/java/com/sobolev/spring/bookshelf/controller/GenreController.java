package com.sobolev.spring.bookshelf.controller;

import com.sobolev.spring.bookshelf.dto.request.GenreRequest;
import com.sobolev.spring.bookshelf.dto.response.GenreResponse;
import com.sobolev.spring.bookshelf.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<List<GenreResponse>> findAll() {
        return new ResponseEntity<>(genreService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> findById(@PathVariable Long id) {
        return new ResponseEntity<>(genreService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<GenreResponse> getGenreByName(@PathVariable String name) {
        return new ResponseEntity<>(genreService.findByName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GenreResponse> createGenre(@Valid @RequestBody GenreRequest genreRequest) {
        return new ResponseEntity<>(genreService.create(genreRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> updateGenre(@Valid @RequestBody GenreRequest genreRequest,
                                                     @PathVariable Long id) {
        return new ResponseEntity<>(genreService.update(id, genreRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        boolean success = genreService.deleteById(id);
        return new ResponseEntity<>(success ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
