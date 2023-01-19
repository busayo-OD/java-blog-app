package com.springboot.blog.service.impl;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Blog;
import com.springboot.blog.model.Comment;
import com.springboot.blog.repository.BlogRepository;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private BlogRepository blogRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BlogRepository blogRepository) {
        this.commentRepository = commentRepository;
        this.blogRepository = blogRepository;
    }

    @Override
    public CommentDto createComment(Long blogId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

//        retrieve blog entity by id
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new ResourceNotFoundException("Blog", "id",blogId));

//        set blog to comment entity
        comment.setBlog(blog);

//        save comment entity to the database
        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByBlogId(Long blogId) {
//        retrieve comments by blogId
        List<Comment> comments = commentRepository.findByBlogId(blogId);

//        convert list of comment entities to list of comment dtos
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long blogId, Long commentId) {
//        retrieve blog entity by id
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new ResourceNotFoundException("Blog", "id",blogId));

//        retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id",commentId));

        if(!comment.getBlog().getId().equals(blog.getId())){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to the blog");
        }

        return mapToDto(comment);

    }

    @Override
    public CommentDto updateComment(Long blogId, Long commentId, CommentDto commentRequest) {
        //        retrieve blog entity by id
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new ResourceNotFoundException("Blog", "id",blogId));

//               retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id",commentId));

        if(!comment.getBlog().getId().equals(blog.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to blog");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long blogId, Long commentId) {

//        retrieve comment by id
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new ResourceNotFoundException("Blog", "id",blogId));

//        retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id",commentId));

        if(!comment.getBlog().getId().equals(blog.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to blog");
        }

        commentRepository.delete(comment);

    }

    private CommentDto mapToDto (Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());

        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        return comment;

    }
}
