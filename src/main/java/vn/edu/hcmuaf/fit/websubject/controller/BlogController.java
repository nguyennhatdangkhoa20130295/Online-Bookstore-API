package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmuaf.fit.websubject.model.Blog;
import vn.edu.hcmuaf.fit.websubject.model.Category;
import vn.edu.hcmuaf.fit.websubject.payload.response.MessageResponse;
import vn.edu.hcmuaf.fit.websubject.service.BlogService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    @GetMapping("/all")
    public List<Blog> getAllBlogs(){
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
//    @GetMapping("/blogCate/{id}")
//    public ResponseEntity<Blog> getBlogByCate(@PathVariable int id) {
//        Optional<Blog> blog = blogService.getBlogByCate(id);
//        if (blog.isPresent()) {
//            return ResponseEntity.ok(blog.get());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

}
