package vn.edu.hcmuaf.fit.websubject.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.*;
import vn.edu.hcmuaf.fit.websubject.repository.BlogCateRepository;
import vn.edu.hcmuaf.fit.websubject.repository.BlogRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.BlogService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    private BlogRepository blogRepository;
    private UserRepository userRepository;
    private BlogCateRepository blogCateRepository;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository, UserRepository userRepository, BlogCateRepository blogCateRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.blogCateRepository=blogCateRepository;
    }


    public Page<Blog> getAllBlogs(int page, int perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        return blogRepository.findAll(pageable);
    }
    public List<Blog> getAllBlogsUser() {
        return blogRepository.findAll();
    }

    public Page<Blog> findAll(int page, int size, String sort, String order, String filter) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Sort sortPa = Sort.by(direction, sort);
        Pageable pageable = PageRequest.of(page, size, sortPa);

        JsonNode jsonFilter;
        try {
            jsonFilter = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Specification<Blog> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (jsonFilter.has("q")) {
                String searchStr = jsonFilter.get("q").asText();
                predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + searchStr.toLowerCase() + "%");
            }
            return predicate;
        };

        return blogRepository.findAll(specification, pageable);
    }

    @Override
    public void addBlog(int blogCate, String title, String content, String image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(customUserDetails.getUsername());
        if (user.isPresent()) {
            User currentUser = user.get();
            Optional<BlogCategory> blogCategory = blogCateRepository.findById(blogCate);
            if (blogCategory.isPresent()) {
                BlogCategory presentBlogCate = blogCategory.get();
                Blog newBlog = new Blog();
                newBlog.setBlogCate(presentBlogCate);
                newBlog.setCreatedBy(currentUser);
                newBlog.setTitle(title);
                newBlog.setContent(content);
                if(content.length() > 100){
                    newBlog.setShortDesc(content.substring(0, 100));
                } else {
                    newBlog.setShortDesc(content);
                }
                newBlog.setImage(image);
                newBlog.setUpdateBy(currentUser);
                newBlog.setCreatedAt(getCurrentTimeInVietnam());
                newBlog.setUpdatedAt(getCurrentTimeInVietnam());
                blogRepository.save(newBlog);
            }
        } else {
            System.out.println("Không tìm thấy user hiện tại");
        }
    }

    @Override
    public void editBlog(int id, int blogCate, String title, String content, String image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(customUserDetails.getUsername());
        if (user.isPresent()) {
            User currentUser = user.get();
            Optional<BlogCategory> blogCategory = blogCateRepository.findByBlogId(blogCate);
            if (blogCategory.isPresent()) {
                BlogCategory presentBlogCate = blogCategory.get();
                Optional<Blog> blog = blogRepository.findById(id);
                if (blog.isPresent()) {
                    Blog presentBlog = blog.get();
                    presentBlog.setBlogCate(presentBlogCate);
                    presentBlog.setUpdateBy(currentUser);
                    presentBlog.setTitle(title);
                    presentBlog.setContent(content);
                    if(content.length() > 100){
                        presentBlog.setShortDesc(content.substring(0, 100));
                    } else {
                        presentBlog.setShortDesc(content);
                    }
                    presentBlog.setImage(image);
                    presentBlog.setUpdatedAt(getCurrentTimeInVietnam());
                    blogRepository.save(presentBlog);
                }
            }
        } else {
            System.out.println("Không tìm thấy user hiện tại");
        }
    }
    @Override
    public void deleteBlog(int id) {
        blogRepository.deleteById(id);
    }
    public static Date getCurrentTimeInVietnam() {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }
    public Optional<Blog> getBlogById(int id) {
        return blogRepository.findById(id);
    }

//    public Optional<Blog> getBlogByCate(int cateId) {
//        return blogRepository.findByBlogCateId(cateId);
//    }
}
