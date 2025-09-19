package com.sobolev.spring.bookshelf.dto.response;

import com.sobolev.spring.bookshelf.model.BookStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer publicationYear;
    private BookStatus status;
}
