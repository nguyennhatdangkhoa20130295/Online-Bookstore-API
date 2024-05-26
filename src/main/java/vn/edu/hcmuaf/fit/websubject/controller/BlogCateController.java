package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.websubject.entity.BlogCategory;
import vn.edu.hcmuaf.fit.websubject.service.BlogCateService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blogCate")
public class BlogCateController {
    BlogCateService blogCateService;
    @Autowired
    public BlogCateController(BlogCateService blogCateService) {
        this.blogCateService = blogCateService;
    }

    @GetMapping("/all")
    public List<BlogCategory> getAllCates() {
        return blogCateService.getAllCate();
    }

    @GetMapping("")
    public ResponseEntity<Page<BlogCategory>> getAllBlogCate(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "") String filter,
                                                  @RequestParam(defaultValue = "25") int perPage,
                                                  @RequestParam(defaultValue = "id") String sort,
                                                  @RequestParam(defaultValue = "ASC") String order) {
        try {
            Page<BlogCategory> blogCate = blogCateService.findAll(page, perPage, sort, order, filter);
            return ResponseEntity.ok(blogCate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogCategory> getCateById(@PathVariable int id) {
        Optional<BlogCategory> blogcate = blogCateService.getCateById(id);
        if (blogcate.isPresent()) {
            return ResponseEntity.ok(blogcate.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
