package com.springboot.blog.dto;

import com.springboot.blog.annotation.BlogStateAnnotation;
import com.springboot.blog.model.enums.BlogState;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BlogStateDto {

    @BlogStateAnnotation(enumClass = BlogState.class)
    private String state;
}
