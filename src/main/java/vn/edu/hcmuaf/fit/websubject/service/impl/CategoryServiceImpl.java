package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Category;
import vn.edu.hcmuaf.fit.websubject.repository.CategoryRepository;
import vn.edu.hcmuaf.fit.websubject.service.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getMainCategories() {
        return categoryRepository.findByParentCategoryIsNull();
    }

    @Override
    public List<Category> getSubCategories(Integer parentId) {
        return categoryRepository.findByParentCategoryId(parentId);
    }

    @Override
    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Integer id, Category category) {
        if (categoryRepository.existsById(id)) {
            category.setId(id);
            return categoryRepository.save(category);
        } else {
            return null;
        }
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}
