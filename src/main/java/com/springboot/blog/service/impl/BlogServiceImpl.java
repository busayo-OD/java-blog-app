package com.springboot.blog.service.impl;

import com.springboot.blog.dto.BlogDto;
import com.springboot.blog.dto.BlogResponse;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Blog;
import com.springboot.blog.repository.BlogRepository;
import com.springboot.blog.service.BlogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

//
        Blog blog = mapToEntity(blogDto);
        Blog newBlog = blogRepository.save(blog);

//        convert entity to dto
        BlogDto blogResponse = mapToDto(newBlog);
        return blogResponse;
    }

    @Override
    public BlogResponse getAllBlogs(int pageNo, int pageSize) {

//      create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Blog> blogs = blogRepository.findAll(pageable);

//        get content for page object
        List<Blog> blogList = blogs.getContent();

       List<BlogDto> content = blogList.stream().map(blog -> mapToDto(blog)).collect(Collectors.toList());

        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setContent(content);
        blogResponse.setPageNo(blogs.getNumber());
        blogResponse.setPageSize(blogs.getSize());
        blogResponse.setTotalElements(blogs.getTotalElements());
        blogResponse.setTotalPages(blogs.getTotalPages());
        blogResponse.setLast(blogs.isLast());

        return blogResponse;
    }

    @Override
    public BlogDto getBlogById(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blog", "id", id ));
        return mapToDto(blog);
    }

    @Override
    public BlogDto updateBlog(BlogDto blogDto, Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blog", "id", id ));
        blog.setTitle(blogDto.getTitle());
        blog.setDescription(blogDto.getDescription());
        blog.setContent(blogDto.getContent());

        Blog updatedBlog = blogRepository.save(blog);
        return mapToDto(updatedBlog);
    }

    @Override
    public void deleteBlog(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blog", "id", id ));
        blogRepository.delete(blog);
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
