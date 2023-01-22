package com.springboot.blog.controller;

import com.springboot.blog.dto.BlogDto;
import com.springboot.blog.dto.BlogResponse;
import com.springboot.blog.service.BlogService;
import com.springboot.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/blogs")
public class BlogController {

    private BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public ResponseEntity<BlogDto> createBlog(@Valid  @RequestBody BlogDto blogDto){
        return new ResponseEntity<>(blogService.createBlog(blogDto), HttpStatus.CREATED);
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
    public ResponseEntity<BlogDto> getBlogById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogDto> updateBlog(@Valid @RequestBody BlogDto blogDto, @PathVariable(name = "id") Long id){
        return ResponseEntity.ok(blogService.updateBlog(blogDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlogById(@PathVariable(name = "id") Long id){
        blogService.deleteBlog(id);
        return new ResponseEntity("Blog deleted successfully", HttpStatus.OK);
    }
}
