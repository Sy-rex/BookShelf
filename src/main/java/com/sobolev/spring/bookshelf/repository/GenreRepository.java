package com.sobolev.spring.bookshelf.repository;

import com.sobolev.spring.bookshelf.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);

    long countByIdIn(Collection<Long> ids);

    List<Genre> findByNameContaining(String name);

    List<Genre> findByNameStartingWith(String prefix);

    long countByName(String name);
}
