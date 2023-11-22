package com.springboot.blog.repository;

import com.springboot.blog.model.Blog;
import com.springboot.blog.model.enums.BlogState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByCategoryIdAndState(Long categoryId, BlogState blogState);
    Optional<Blog> findByIdAndUserId(Long id, Long userId);
    Optional<Blog> findByIdAndState(Long id, BlogState blogState);
    Page<Blog> findByState(BlogState blogState, Pageable pageable);
    Page<Blog> findByUserId(Long userId, Pageable pageable);
}
