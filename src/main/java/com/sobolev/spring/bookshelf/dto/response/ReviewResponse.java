package com.sobolev.spring.bookshelf.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
    private BookResponse book;
}
