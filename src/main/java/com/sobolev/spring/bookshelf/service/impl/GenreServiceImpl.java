package com.sobolev.spring.bookshelf.service.impl;

import com.sobolev.spring.bookshelf.dto.request.GenreRequest;
import com.sobolev.spring.bookshelf.dto.response.GenreResponse;
import com.sobolev.spring.bookshelf.exception.NotFoundGenreException;
import com.sobolev.spring.bookshelf.model.Genre;
import com.sobolev.spring.bookshelf.repository.GenreRepository;
import com.sobolev.spring.bookshelf.service.GenreService;
import com.sobolev.spring.bookshelf.util.GenreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
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
        log.info("Start find all genres");
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenreResponse> findById(Long id) {
        log.info("Start find genre by id");
        return genreRepository.findById(id).map(genreMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenreResponse> findByName(String name) {
        log.info("Start find genre by name: {}", name);
        return genreRepository.findByName(name).map(genreMapper::toResponse);
    }

    @Override
    public GenreResponse create(GenreRequest genreRequest) {
        log.info("Start create genre");
        Genre genre = genreMapper.toEntity(genreRequest);
        Genre savedGenre = genreRepository.save(genre);
        log.info("End create genre");
        return genreMapper.toResponse(savedGenre);
    }

    @Override
    public Optional<GenreResponse> update(Long id, GenreRequest genreRequest) {
        log.info("Start update genre");
        return genreRepository.findById(id)
                .map(existingGenre -> {
                    genreMapper.updateEntityFromRequest(genreRequest, existingGenre);
                    Genre updatedGenre = genreRepository.save(existingGenre);
                    log.info("End update genre");
                    return genreMapper.toResponse(updatedGenre);
                });
    }

    @Override
    public boolean deleteById(Long id) {
        if (genreRepository.existsById(id)) {
            log.info("delete genre by id");
            genreRepository.deleteById(id);
            return true;
        }
        log.info("not found genre by id");
        return false;
    }
}
