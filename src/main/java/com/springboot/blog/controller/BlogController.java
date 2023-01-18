package com.springboot.blog.controller;

import com.springboot.blog.dto.BlogDto;
import com.springboot.blog.dto.BlogResponse;
import com.springboot.blog.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/blogs")
public class BlogController {

    private BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public ResponseEntity<BlogDto> createBlog(@RequestBody BlogDto blogDto){
        return new ResponseEntity<>(blogService.createBlog(blogDto), HttpStatus.CREATED);
    }

    @GetMapping()
    public BlogResponse getAllBlogs(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ){
        return blogService.getAllBlogs(pageNo, pageSize);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogDto> getBlogById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogDto> updateBlog(@RequestBody BlogDto blogDto, @PathVariable(name = "id") Long id){
        return ResponseEntity.ok(blogService.updateBlog(blogDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlogById(@PathVariable(name = "id") Long id){
        blogService.deleteBlog(id);
        return new ResponseEntity("Blog deleted successfully", HttpStatus.OK);
    }
}
