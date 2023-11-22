package com.springboot.blog.repository;

import com.springboot.blog.model.Blog;
import com.springboot.blog.model.enums.BlogState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByCategoryIdAndState(Long categoryId, BlogState blogState);
    Optional<Blog> findByIdAndUserId(Long id, Long userId);
    Optional<Blog> findByIdAndState(Long id, BlogState blogState);
    Page<Blog> findByState(BlogState blogState, Pageable pageable);
    Page<Blog> findByUserId(Long userId, Pageable pageable);
    Page<Blog> findByStateAndUserId(BlogState blogState, Long userId, Pageable pageable);
    Page<Blog> findByStateAndUserIdOrStateIsNull(BlogState blogState, Long userId, Pageable pageable);
    @Query("SELECT DISTINCT b FROM Blog b " +
            "LEFT JOIN b.user u " +
            "LEFT JOIN b.tags t " +
            "WHERE " +
            "   b.state = 'PUBLISHED' AND " +
            "   (UPPER(u.firstName) LIKE UPPER(CONCAT('%', :searchTerm, '%')) OR " +
            "   UPPER(u.lastName) LIKE UPPER(CONCAT('%', :searchTerm, '%')) OR " +
            "   UPPER(u.username) LIKE UPPER(CONCAT('%', :searchTerm, '%')) OR " +
            "   UPPER(b.title) LIKE UPPER(CONCAT('%', :searchTerm, '%')) OR " +
            "   UPPER(t.name) LIKE UPPER(CONCAT('%', :searchTerm, '%')))")
    Page<Blog> searchPublishedBlogsByUserAndTagsAndTitle(
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
}
