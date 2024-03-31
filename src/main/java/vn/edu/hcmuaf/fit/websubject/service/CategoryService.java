package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    List<Category> getSubCategories(Long parentId);
}
