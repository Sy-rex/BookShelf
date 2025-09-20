package com.sobolev.spring.bookshelf.service.impl;

import com.sobolev.spring.bookshelf.dto.request.GenreRequest;
import com.sobolev.spring.bookshelf.dto.response.GenreResponse;
import com.sobolev.spring.bookshelf.exception.NotFoundGenreException;
import com.sobolev.spring.bookshelf.model.Genre;
import com.sobolev.spring.bookshelf.repository.GenreRepository;
import com.sobolev.spring.bookshelf.service.GenreService;
import com.sobolev.spring.bookshelf.util.GenreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreResponse> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GenreResponse findById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundGenreException("Genre not found with id: " + id));
        return genreMapper.toResponse(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public GenreResponse findByName(String name) {
        Genre genre = genreRepository.findByName(name)
                .orElseThrow(() -> new NotFoundGenreException("Genre not found with name: " + name));

        return genreMapper.toResponse(genre);
    }

    @Override
    public GenreResponse create(GenreRequest genreRequest) {
        Genre genre = genreMapper.toEntity(genreRequest);
        Genre savedGenre = genreRepository.save(genre);
        return genreMapper.toResponse(savedGenre);
    }

    @Override
    public GenreResponse update(Long id, GenreRequest genreRequest) {
        return genreRepository.findById(id)
                .map(existingGenre -> {
                    genreMapper.updateEntityFromRequest(genreRequest, existingGenre);
                    Genre updatedGenre = genreRepository.save(existingGenre);
                    return genreMapper.toResponse(updatedGenre);
                }).orElseThrow(() -> new NotFoundGenreException("Genre not found with id: " + id));
    }

    @Override
    public boolean deleteById(Long id) {
        if (genreRepository.existsById(id)) {
            genreRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
