package com.springboot.blog.service.impl;

import com.springboot.blog.dto.BlogDto;
import com.springboot.blog.dto.BlogInfoDto;
import com.springboot.blog.dto.BlogResponse;
import com.springboot.blog.dto.TagDto;
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

//        create a sort object
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

//      create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Blog> blogs = blogRepository.findAll(pageable);

//        get content for page object
        List<Blog> blogList = blogs.getContent();

       List<BlogInfoDto> content = blogList.stream().map(this::mapToBlogInfoDto).collect(Collectors.toList());

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
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new BlogNotFoundException(id));
        // Increment the reading count
        blog.setReadingCount(blog.getReadingCount() + 1);
        blogRepository.save(blog);
        return mapToBlogInfoDto(blog);
    }

    @Override
    public BlogInfoDto updateBlog(BlogDto blogDto, Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new BlogNotFoundException(id));
        Long categoryId = blogDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        blog.setTitle(blogDto.getTitle());
        blog.setDescription(blogDto.getDescription());
        blog.setContent(blogDto.getContent());
        blog.setCategory(category);

        Blog updatedBlog = blogRepository.save(blog);
        return mapToBlogInfoDto(updatedBlog);
    }

    @Override
    public boolean deleteBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new BlogNotFoundException(id));
        blogRepository.delete(blog);
        return true;
    }

    @Override
    public List<BlogInfoDto> getBlogsByCategory(Long categoryId) {

        List<Blog> blogs = blogRepository.findByCategoryId(categoryId);
        return blogs.stream().map(this::mapToBlogInfoDto)
                .collect(Collectors.toList());
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
        blogInfoDto.setTags(blog.getTags().stream().map(this::mapToTagDto)
                .collect(Collectors.toList()));
        return blogInfoDto;
    }

    private TagDto mapToTagDto (Tag tag){
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
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
