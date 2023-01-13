package com.springboot.blog.service;

import com.springboot.blog.dto.BlogDto;

public interface BlogService {
    BlogDto createBlog(BlogDto blogDto);
}
