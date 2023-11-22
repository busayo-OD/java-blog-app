package com.springboot.blog.service.impl;

import com.springboot.blog.dto.*;
import com.springboot.blog.exception.BlogNotFoundException;
import com.springboot.blog.exception.CategoryNotFoundException;
import com.springboot.blog.exception.UserNotFoundException;
import com.springboot.blog.model.Blog;
import com.springboot.blog.model.Category;
import com.springboot.blog.model.Tag;
import com.springboot.blog.model.User;
import com.springboot.blog.model.enums.BlogState;
import com.springboot.blog.repository.BlogRepository;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.TagRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.BlogService;
import com.springboot.blog.utils.BlogUtil;
import com.springboot.blog.utils.ReadingTimeCalculator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public BlogServiceImpl(BlogRepository blogRepository, CategoryRepository categoryRepository,
                           TagRepository tagRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean createBlog(Long userId, BlogDto blogDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Long categoryId = blogDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

//        convert dto to entity
        Blog blog = mapToEntity(blogDto);
        blog.setCategory(category);
        blog.setUser(user);
        blogRepository.save(blog);

        return true;
    }

    @Override
    public BlogResponse getAllBlogs(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Create a sort object
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Retrieve only published blogs
        Page<Blog> blogs = blogRepository.findByState(BlogState.Published, pageable);

        // Get content for page object
        List<Blog> blogList = blogs.getContent();

        List<BlogInfo2Dto> content = blogList.stream().map(this::mapToBlogInfo2Dto).collect(Collectors.toList());

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
    public BlogResponse getMyBlogs(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        // Create a sort object
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Retrieve only published blogs
        Page<Blog> blogs = blogRepository.findByUserId(userId, pageable);

        // Get content for page object
        List<Blog> blogList = blogs.getContent();

        List<BlogInfo2Dto> content = blogList.stream().map(this::mapToBlogInfo2Dto).collect(Collectors.toList());

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
    public BlogInfoDto getBlogById(Long id) {
        Blog blog = blogRepository.findByIdAndState(id, BlogState.Published)
                .orElseThrow(() -> new BlogNotFoundException(id));
        // Increment the reading count
        blog.setReadingCount(blog.getReadingCount() + 1);
        blogRepository.save(blog);
        return mapToBlogInfoDto(blog);
    }

    @Override
    public BlogInfoDto updateBlog(Long userId, BlogDto blogDto, Long id) {
        Blog blog = blogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BlogNotFoundException(id));
        Long categoryId = blogDto.getCategoryId();
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException(categoryId));
            blog.setCategory(category);
        }
        if (!blogDto.getTitle().isEmpty()){
            blog.setTitle(blogDto.getTitle());
        }
        if (!blogDto.getDescription().isEmpty()){
            blog.setDescription(blogDto.getDescription());
        }
        if (!blogDto.getContent().isEmpty()){
            blog.setContent(blogDto.getContent());
        }

        Blog updatedBlog = blogRepository.save(blog);
        return mapToBlogInfoDto(updatedBlog);
    }

    @Override
    public boolean deleteBlog(Long userId, Long id) {
        Blog blog = blogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BlogNotFoundException(id));
        blogRepository.delete(blog);
        return true;
    }

    @Override
    public List<BlogInfo2Dto> getBlogsByCategory(Long categoryId) {

        List<Blog> blogs = blogRepository.findByCategoryIdAndState(categoryId, BlogState.Published);
        return blogs.stream().map(this::mapToBlogInfo2Dto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateBlogState(Long userId, Long id, BlogStateDto blogStateDto) {
        Blog blog = blogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BlogNotFoundException(id));
        blog.setState(BlogUtil.getBlogState(blogStateDto.getState()));
        blogRepository.save(blog);
        return true;
    }

    private BlogInfoDto mapToBlogInfoDto(Blog blog){
        BlogInfoDto blogInfoDto = new BlogInfoDto();
        blogInfoDto.setId(blog.getId());
        blogInfoDto.setTitle(blog.getTitle());
        blogInfoDto.setDescription(blog.getDescription());
        blogInfoDto.setContent(blog.getContent());
        blogInfoDto.setCategory(blog.getCategory());
        blogInfoDto.setState(String.valueOf(blog.getState()));
        blogInfoDto.setReadingCount(blog.getReadingCount());
        blogInfoDto.setReadingTime(blog.getReadingTime());
        blogInfoDto.setCreatedOn(blog.getCreatedOn());
        blogInfoDto.setUpdatedOn(blog.getUpdatedOn());
        blogInfoDto.setOwner(mapToUserInfoDto(blog.getUser()));
        blogInfoDto.setTags(blog.getTags().stream().map(this::mapToTagDto)
                .collect(Collectors.toList()));
        return blogInfoDto;
    }

    private BlogInfo2Dto mapToBlogInfo2Dto(Blog blog){
        BlogInfo2Dto blogInfo2Dto = new BlogInfo2Dto();
        blogInfo2Dto.setId(blog.getId());
        blogInfo2Dto.setTitle(blog.getTitle());
        blogInfo2Dto.setDescription(blog.getDescription());
        blogInfo2Dto.setContent(blog.getContent());
        blogInfo2Dto.setCategory(blog.getCategory());
        blogInfo2Dto.setState(String.valueOf(blog.getState()));
        blogInfo2Dto.setReadingCount(blog.getReadingCount());
        blogInfo2Dto.setReadingTime(blog.getReadingTime());
        blogInfo2Dto.setCreatedOn(blog.getCreatedOn());
        blogInfo2Dto.setUpdatedOn(blog.getUpdatedOn());
        blogInfo2Dto.setTags(blog.getTags().stream().map(this::mapToTagDto)
                .collect(Collectors.toList()));
        return blogInfo2Dto;
    }


    private TagDto mapToTagDto (Tag tag){
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    private UserInfoDto mapToUserInfoDto (User user){
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setFirstName(user.getFirstName());
        userInfoDto.setLastName(user.getLastName());
        userInfoDto.setUsername(user.getUsername());
        userInfoDto.setEmail(user.getEmail());
        return userInfoDto;
    }

    //        convert dto to entity
    private Blog mapToEntity(BlogDto blogDto){
        Blog blog = new Blog();
        blog.setTitle(blogDto.getTitle());
        blog.setDescription(blogDto.getDescription());
        blog.setContent(blogDto.getContent());
        blog.setState(BlogState.Draft);
        blogDto.getTags().forEach(tagDto -> {
            Tag tag = createTagFromDTO(tagDto);
            tag.setBlog(blog);
            tagRepository.save(tag);
        });
        // Calculate and set the reading time
        String readingTime = ReadingTimeCalculator.calculateReadingTime(blogDto.getContent());
        blog.setReadingTime(readingTime);
        return blog;
    }

    private Tag createTagFromDTO(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        return tag;
    }

}
