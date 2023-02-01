package com.springboot.blog.repository;

import com.springboot.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByCategoryId(Long categoryId);
}
