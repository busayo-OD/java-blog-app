package com.springboot.blog.service.impl;

import com.springboot.blog.dto.BlogDto;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Blog;
import com.springboot.blog.repository.BlogRepository;
import com.springboot.blog.service.BlogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    private BlogRepository blogRepository;

    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public BlogDto createBlog(BlogDto blogDto) {
        Blog blog = mapToEntity(blogDto);
        Blog newBlog = blogRepository.save(blog);

        BlogDto blogResponse = mapToDto(newBlog);
        return blogResponse;
    }

    @Override
    public List<BlogDto> getAllBlogs() {
       List<Blog> blogs = blogRepository.findAll();
       return blogs.stream().map(blog -> mapToDto(blog)).collect(Collectors.toList());

    }

    @Override
    public BlogDto getBlogById(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blog", "id", id ));
        return mapToDto(blog);
    }

    //        convert entity to dto
    private BlogDto mapToDto(Blog blog){
        BlogDto blogDto = new BlogDto();
        blogDto.setId(blog.getId());
        blogDto.setTitle(blog.getTitle());
        blogDto.setDescription(blog.getDescription());
        blogDto.setContent(blog.getContent());

        return blogDto;
    }

    //        convert dto to entity
    private Blog mapToEntity(BlogDto blogDto){
        Blog blog = new Blog();
        blog.setTitle(blogDto.getTitle());
        blog.setDescription(blogDto.getDescription());
        blog.setContent(blogDto.getContent());

        return blog;
    }

}
