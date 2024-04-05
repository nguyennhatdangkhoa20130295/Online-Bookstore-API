package vn.edu.hcmuaf.fit.websubject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.BlogCategory;
import vn.edu.hcmuaf.fit.websubject.repository.BlogCateRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BlogCateService {
    @Autowired
    BlogCateRepository blogCateRepository;

    public List<BlogCategory> getAllCate() {
        return blogCateRepository.findAll();
    }
    public Optional<BlogCategory> getCateById(int id) {
        return blogCateRepository.findById(id);
    }
}
