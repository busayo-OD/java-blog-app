package com.springboot.blog.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BlogDto {
    private Long id;

    private String title;

    private String description;

    private String content;

    private Long categoryId;

    private List<TagDto> tags;

}
