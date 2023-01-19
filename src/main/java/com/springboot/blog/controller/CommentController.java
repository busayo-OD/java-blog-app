package com.springboot.blog.controller;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping("blogs/{blogId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "blogId") Long blogId, @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(blogId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("blogs/{blogId}/comments")
    public List<CommentDto> getCommentsByBlogId(@PathVariable(value = "blogId")Long blogId){
        return commentService.getCommentsByBlogId(blogId);
    }

    @GetMapping("blogs/{blogId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "blogId") Long blogId,
                                                     @PathVariable(value = "id") Long commentId) {
        CommentDto commentDto = commentService.getCommentById(blogId, commentId);
        return  new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PutMapping("blogs/{blogId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "blogId") Long blogId,
                                                    @PathVariable(value = "id") Long commentId,
                                                    @RequestBody CommentDto commentDto){
        CommentDto updatedComment = commentService.updateComment(blogId,commentId, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/blogs/{blogId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "blogId") Long blogId,
                                                @PathVariable(value = "id") Long commentId){
        commentService.deleteComment(blogId, commentId);
        return  new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);

    }

}
