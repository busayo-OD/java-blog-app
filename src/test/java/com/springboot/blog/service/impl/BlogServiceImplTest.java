package com.springboot.blog.service.impl;

import com.springboot.blog.dto.BlogInfoDto;
import com.springboot.blog.exception.BlogNotFoundException;
import com.springboot.blog.model.Blog;
import com.springboot.blog.model.Tag;
import com.springboot.blog.model.User;
import com.springboot.blog.model.enums.BlogState;
import com.springboot.blog.repository.BlogRepository;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.TagRepository;
import com.springboot.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BlogServiceImpl blogService;

    @Test
    void getBlogById() {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Should_ReturnBlogInfoDto_When_BlogIsFound() {
        Long blogId = 1L;
        Blog blog = new Blog();
        blog.setId(blogId);
        blog.setState(BlogState.Published);

        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        tag.setName("SampleTag");
        tags.add(tag);
        blog.setTags(tags);

        User user = new User();
        user.setFirstName("TestFirstName");
        blog.setUser(user);

        when(blogRepository.findByIdAndState(blogId, BlogState.Published)).thenReturn(Optional.of(blog));

        BlogInfoDto response = blogService.getBlogById(blogId);

        assertNotNull(response);
        assertEquals(blogId, response.getId());

        verify(blogRepository, times(1)).findByIdAndState(blogId, BlogState.Published);
    }

    @Test
    void Should_ThrowBlogNotFoundException_When_BlogIsNotFound() {
        Long nonExistentBlogId = 999L;

        when(blogRepository.findByIdAndState(nonExistentBlogId, BlogState.Published)).thenReturn(Optional.empty());

        BlogNotFoundException exception = assertThrows(BlogNotFoundException.class, () -> {
            blogService.getBlogById(nonExistentBlogId);
        });

        assertEquals("Blog with an id " + nonExistentBlogId + " cannot be found or does not exist in record.", exception.getMessage());

        verify(blogRepository, times(1)).findByIdAndState(nonExistentBlogId, BlogState.Published);
    }


}