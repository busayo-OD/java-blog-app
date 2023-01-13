package com.springboot.blog.service.impl;

import com.springboot.blog.dto.BlogDto;
import com.springboot.blog.model.Blog;
import com.springboot.blog.repository.BlogRepository;
import com.springboot.blog.service.BlogService;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {

    private BlogRepository blogRepository;

    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public BlogDto createBlog(BlogDto blogDto) {

//        convert dto to entity
        Blog blog = new Blog();
        blog.setTitle(blogDto.getTitle());
        blog.setDescription(blogDto.getDescription());
        blog.setContent(blogDto.getContent());
        Blog newBlog = blogRepository.save(blog);

//        convert entity to dto
        BlogDto blogResponse = new BlogDto();
        blogResponse.setId(newBlog.getId());
        blogResponse.setTitle(newBlog.getTitle());
        blogResponse.setDescription(newBlog.getDescription());
        blogResponse.setContent(newBlog.getContent());

        return blogResponse;
    }
}
