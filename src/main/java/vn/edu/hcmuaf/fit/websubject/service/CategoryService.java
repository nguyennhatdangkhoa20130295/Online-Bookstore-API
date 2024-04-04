package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();

    List<Category> getMainCategories();

    List<Category> getSubCategories(Integer parentId);

    Optional<Category> getCategoryById(Integer id);

    Category createCategory(Category category);

    Category updateCategory(Integer id, Category category);

    void deleteCategory(Integer id);

}
