package com.springboot.blog.service;

import com.springboot.blog.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long blogId, CommentDto commentDto);

    List<CommentDto> getCommentsByBlogId(Long blogId);

    CommentDto getCommentById(Long blogId, Long commentId);

    CommentDto updateComment(Long blogId, Long commentId, CommentDto commentRequest);

    void deleteComment(Long blogId, Long commentId);
}
