package com.springboot.blog.service;

import com.springboot.blog.dto.BlogDto;

import java.util.List;

public interface BlogService {
    BlogDto createBlog(BlogDto blogDto);
    List<BlogDto> getAllBlogs();
    BlogDto getBlogById(Long id);
    BlogDto updateBlog(BlogDto blogDto, Long id);
}
