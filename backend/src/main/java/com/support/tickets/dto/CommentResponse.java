package com.support.tickets.dto;

import com.support.tickets.domain.Comment;

import java.time.Instant;

public record CommentResponse(
        Long id,
        String author,
        String body,
        Instant createdAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getAuthor(),
                comment.getBody(),
                comment.getCreatedAt()
        );
    }
}
