package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.entity.BlogCategory;

import java.util.List;
import java.util.Optional;

public interface BlogCateService {
    List<BlogCategory> getAllCate();

    Optional<BlogCategory> getCateById(int id);
}
