package com.springboot.blog.controller;

import com.springboot.blog.dto.*;
import com.springboot.blog.model.enums.BlogState;
import com.springboot.blog.service.BlogService;
import com.springboot.blog.utils.AppConstants;
import javax.validation.Valid;

import com.springboot.blog.utils.CurrentUserUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/blogs")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }


    @PostMapping("/add")
    public boolean createBlog(@Valid  @RequestBody BlogDto blogDto){
        Long userId = Objects.requireNonNull(CurrentUserUtil.getCurrentUser()).getId();
        return blogService.createBlog(userId,blogDto);
    }

    @GetMapping()
    public BlogResponse getAllBlogs(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return blogService.getAllBlogs(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/me")
    public BlogResponse getMyBlogs(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "blogState", required = false) BlogState blogState
    ){
        Long userId = Objects.requireNonNull(CurrentUserUtil.getCurrentUser()).getId();
        return blogService.getMyBlogs(userId, pageNo, pageSize, sortBy, sortDir, blogState);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BlogInfoDto> getBlogById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<BlogInfoDto> updateBlog(@Valid @RequestBody BlogDto blogDto, @PathVariable(name = "id") Long id){
        Long userId = Objects.requireNonNull(CurrentUserUtil.getCurrentUser()).getId();
        return ResponseEntity.ok(blogService.updateBlog(userId, blogDto, id));
    }

    @PutMapping("/edit/state/{id}")
    public boolean updateBlogState(@Valid @RequestBody BlogStateDto blogStateDto, @PathVariable(name = "id") Long id){
        Long userId = Objects.requireNonNull(CurrentUserUtil.getCurrentUser()).getId();
        return blogService.updateBlogState(userId,id, blogStateDto);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteBlogById(@PathVariable(name = "id") Long id){
        Long userId = Objects.requireNonNull(CurrentUserUtil.getCurrentUser()).getId();
        return blogService.deleteBlog(userId, id);

    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<BlogInfo2Dto>> getBlogsByCategory(@PathVariable("id") Long categoryId){
        List<BlogInfo2Dto> blogs = blogService.getBlogsByCategory(categoryId);
        return ResponseEntity.ok(blogs);
    }

}
