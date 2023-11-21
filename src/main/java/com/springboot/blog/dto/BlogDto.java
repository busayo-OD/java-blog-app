package com.springboot.blog.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class BlogDto {
    private Long id;

    @NotEmpty
    @Size(min = 2, message = "Blog title must have at least 2 characters")
    private String title;

    @NotEmpty
    @Size(min = 10, message = "Blog description must have at least 10 characters")
    private String description;

    @NotEmpty
    private String content;

    private Set<CommentDto> comments;

    private Long categoryId;

}
