package com.springboot.blog.dto;

import lombok.Data;

@Data
public class BlogDto {
    private Long id;
    private String title;
    private String description;
    private String content;
}
