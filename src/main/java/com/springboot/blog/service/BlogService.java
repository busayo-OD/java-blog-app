package com.springboot.blog.service;

import com.springboot.blog.dto.BlogDto;
import com.springboot.blog.dto.BlogInfoDto;
import com.springboot.blog.dto.BlogResponse;

import java.util.List;

public interface BlogService {
    boolean createBlog(Long userId, BlogDto blogDto);

    BlogResponse getAllBlogs(int pageNo, int pageSize, String sortBy,String sortDir);

    BlogInfoDto getBlogById(Long id);

    BlogInfoDto updateBlog(BlogDto blogDto, Long id);

    boolean deleteBlog(Long id);

    List<BlogInfoDto> getBlogsByCategory(Long categoryId);
}
