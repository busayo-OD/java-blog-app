package com.springboot.blog.controller;

import com.springboot.blog.dto.BlogDto;
import com.springboot.blog.dto.BlogInfoDto;
import com.springboot.blog.dto.BlogResponse;
import com.springboot.blog.service.BlogService;
import com.springboot.blog.utils.AppConstants;
import javax.validation.Valid;

import com.springboot.blog.utils.CurrentUserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/{id}")
    public ResponseEntity<BlogInfoDto> getBlogById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BlogInfoDto> updateBlog(@Valid @RequestBody BlogDto blogDto, @PathVariable(name = "id") Long id){
        return ResponseEntity.ok(blogService.updateBlog(blogDto, id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public boolean deleteBlogById(@PathVariable(name = "id") Long id){
        return blogService.deleteBlog(id);

    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<BlogInfoDto>> getBlogsByCategory(@PathVariable("id") Long categoryId){
        List<BlogInfoDto> blogs = blogService.getBlogsByCategory(categoryId);
        return ResponseEntity.ok(blogs);
    }

}
