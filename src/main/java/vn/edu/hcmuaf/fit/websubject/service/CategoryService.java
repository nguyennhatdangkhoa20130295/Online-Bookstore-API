package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();

    List<Category> getMainCategories();

    Optional<Category> getCategoryById(Long id);

    Category createCategory(Category category);

    Category updateCategory(Long id, Category category);

    void deleteCategory(Long id);

    List<Category> getSubCategories(Long parentId);
}
