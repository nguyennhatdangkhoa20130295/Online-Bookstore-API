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
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Blog;
import vn.edu.hcmuaf.fit.websubject.entity.BlogCategory;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.payload.others.CurrentTime;
import vn.edu.hcmuaf.fit.websubject.repository.BlogCateRepository;
import vn.edu.hcmuaf.fit.websubject.repository.BlogRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.BlogCateService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class BlogCateServiceImpl implements BlogCateService {
    BlogCateRepository blogCateRepository;

    BlogRepository blogRepository;

    UserRepository userRepository;

    @Autowired
    public BlogCateServiceImpl(BlogCateRepository blogCateRepository, UserRepository userRepository, BlogRepository blogRepository) {
        this.blogCateRepository = blogCateRepository;
        this.userRepository = userRepository;
        this.blogRepository = blogRepository;
    }

    public List<BlogCategory> getAllCate() {
        return blogCateRepository.findAll();
    }

    public Page<BlogCategory> getAllBlogCate(int page, int perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        return blogCateRepository.findAll(pageable);
    }

    @Override
    public void addBlogCategory(String name, int createBy, int updateBy) {
        BlogCategory blogCategory = new BlogCategory();
        User user = userRepository.findById(createBy).orElseThrow(() -> new RuntimeException("User not found"));
        blogCategory.setName(name);
        blogCategory.setCreatedBy(user);
        blogCategory.setCreatedAt(CurrentTime.getCurrentTimeInVietnam());
        blogCategory.setUpdatedBy(user);
        blogCategory.setUpdatedAt(CurrentTime.getCurrentTimeInVietnam());
        blogCateRepository.save(blogCategory);
    }

    @Override
    public void editBlogCategory(int id, String name, int updateBy) {
        BlogCategory blogCategory = blogCateRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog category not found"));
        User user = userRepository.findById(updateBy).orElseThrow(() -> new RuntimeException("User not found"));
        blogCategory.setName(name);
        blogCategory.setUpdatedBy(user);
        blogCategory.setUpdatedAt(CurrentTime.getCurrentTimeInVietnam());
        blogCateRepository.save(blogCategory);
    }

    @Override
    public void deleteBlogCategory(int id) {
        if(blogRepository.findAllByBlogCateId(id).size() > 0) {
            throw new RuntimeException("This category is being used by some blogs");
        } else {
            blogCateRepository.deleteById(id);
        }
    }


    public Page<BlogCategory> findAll(int page, int size, String sort, String order, String filter) {
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

        return blogCateRepository.findAll(specification, pageable);
    }

    public Optional<BlogCategory> getCateById(int id) {
        return blogCateRepository.findById(id);
    }

    @Override
    public Optional<BlogCategory> findByBlogId(int id) {
        return blogCateRepository.findByBlogId(id);
    }

}
