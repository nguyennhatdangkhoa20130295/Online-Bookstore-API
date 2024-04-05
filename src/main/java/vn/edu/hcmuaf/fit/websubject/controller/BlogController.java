package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmuaf.fit.websubject.entity.Blog;
import vn.edu.hcmuaf.fit.websubject.entity.BlogCategory;
import vn.edu.hcmuaf.fit.websubject.service.BlogCateService;
import vn.edu.hcmuaf.fit.websubject.service.BlogService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    @Autowired
    BlogCateService blogCateService;

    @GetMapping("/all")
    public List<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable int id) {
        Optional<Blog> blog = blogService.getBlogById(id);
        if (blog.isPresent()) {
            return ResponseEntity.ok(blog.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cate/all")
    public List<BlogCategory> getAllCates() {
        return blogCateService.getAllCate();
    }

    @GetMapping("/cate/{id}")
    public ResponseEntity<BlogCategory> getCateById(@PathVariable int id) {
        Optional<BlogCategory> blogcate = blogCateService.getCateById(id);
        if (blogcate.isPresent()) {
            return ResponseEntity.ok(blogcate.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
