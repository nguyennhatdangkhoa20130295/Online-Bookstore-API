package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.websubject.entity.Blog;
import vn.edu.hcmuaf.fit.websubject.entity.BlogCategory;
import vn.edu.hcmuaf.fit.websubject.payload.request.AddBlogRequest;
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

    @GetMapping("")
    public ResponseEntity<Page<Blog>> getAllBlogs(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "") String filter,
                                                  @RequestParam(defaultValue = "25") int perPage,
                                                  @RequestParam(defaultValue = "title") String sort,
                                                  @RequestParam(defaultValue = "DESC") String order) {
        Page<Blog> blogs = blogService.findAll(page, perPage, sort, order, filter);
        return ResponseEntity.ok(blogs);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Blog>> getAllBlogsUser() {
        List<Blog> blogs = blogService.getAllBlogsUser();
        return ResponseEntity.ok(blogs);
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

    @PostMapping("/add")
//    @PreAuthorize("@authController.hasRole('ADMIN')")
    public ResponseEntity<String> addBlog(@RequestBody AddBlogRequest addBlogRequest) {
        blogService.addBlog(addBlogRequest.getBlogCate(), addBlogRequest.getTitle(), addBlogRequest.getContent(), addBlogRequest.getImage());
        return ResponseEntity.ok("Added blog successfully");
    }
}
