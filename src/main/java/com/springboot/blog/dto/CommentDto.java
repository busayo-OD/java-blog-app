package com.springboot.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;

    @NotEmpty(message = "Name must not be null or empty")
    private String name;

    @NotEmpty(message = "Email must not be null or empty")
    @Email
    private String email;

    @NotEmpty
    @Size(min = 10, message = "Comment body must have at least 10 characters")
    private String body;
}
