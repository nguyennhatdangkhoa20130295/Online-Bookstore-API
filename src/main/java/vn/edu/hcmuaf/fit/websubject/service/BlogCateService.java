package vn.edu.hcmuaf.fit.websubject.service;

import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.model.blog_category;
import vn.edu.hcmuaf.fit.websubject.repository.BlogCateRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BlogCateService {
    BlogCateRepository blogCateRepository;

    public List<blog_category> getAllCate() {
        return blogCateRepository.findAll();
    }
    public Optional<blog_category> getCateById(int id) {
        return blogCateRepository.findById(id);
    }
}
