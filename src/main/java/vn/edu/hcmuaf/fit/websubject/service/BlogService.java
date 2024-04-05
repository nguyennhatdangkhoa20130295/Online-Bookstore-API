package vn.edu.hcmuaf.fit.websubject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Blog;
import vn.edu.hcmuaf.fit.websubject.repository.BlogRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    @Autowired
    BlogRepository blogRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
    public Optional<Blog> getBlogById(int id){
        return blogRepository.findById(id);
    }

//    public Optional<Blog> getBlogByCate(int cateId) {
//        return blogRepository.findByBlogCateId(cateId);
//    }
}
