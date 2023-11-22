package com.springboot.blog.service;

import com.springboot.blog.dto.*;
import com.springboot.blog.model.enums.BlogState;

import java.util.List;

public interface BlogService {
    boolean createBlog(Long userId, BlogDto blogDto);

    BlogResponse getAllBlogs(int pageNo, int pageSize, String sortBy,String sortDir);

    BlogResponse getMyBlogs(Long userId, int pageNo, int pageSize, String sortBy, String sortDir, BlogState blogState);

    BlogInfoDto getBlogById(Long id);

    BlogInfoDto updateBlog(Long userId, BlogDto blogDto, Long id);

    boolean deleteBlog(Long userId, Long id);

    List<BlogInfo2Dto> getBlogsByCategory(Long categoryId);

    boolean updateBlogState (Long userId, Long id, BlogStateDto blogStateDto);

    BlogResponse searchBlogs(String searchTerm, int pageNo, int pageSize, String sortBy, String sortDir);
}
