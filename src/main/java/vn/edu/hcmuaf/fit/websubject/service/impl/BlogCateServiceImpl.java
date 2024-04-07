package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.BlogCategory;
import vn.edu.hcmuaf.fit.websubject.repository.BlogCateRepository;
import vn.edu.hcmuaf.fit.websubject.service.BlogCateService;

import java.util.List;
import java.util.Optional;

@Service
public class BlogCateServiceImpl implements BlogCateService {
    BlogCateRepository blogCateRepository;

    public List<BlogCategory> getAllCate() {
        return blogCateRepository.findAll();
    }

    public Optional<BlogCategory> getCateById(int id) {
        return blogCateRepository.findById(id);
    }
}
