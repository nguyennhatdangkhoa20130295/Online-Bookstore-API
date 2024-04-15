package vn.edu.hcmuaf.fit.websubject.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Category;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.repository.CategoryRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.CategoryService;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static vn.edu.hcmuaf.fit.websubject.payload.others.CurrentTime.getCurrentTimeInVietnam;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> getAllCategories(int page, int perPage, String sort, String filter, String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC"))
            direction = Sort.Direction.DESC;

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Specification<Category> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("name")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + filterJson.get("name").asText() + "%"));
            }
            if (filterJson.has("parentId")) {
                JsonNode parentIdNode = filterJson.get("parentId");

                if (parentIdNode.isNull()) {
                    predicate = criteriaBuilder.and(
                            predicate,
                            criteriaBuilder.isNull(root.get("parentId"))
                    );
                } else {
                    predicate = criteriaBuilder.and(
                            predicate,
                            criteriaBuilder.equal(root.get("parentId"), parentIdNode.asInt())
                    );
                }
            }

            return predicate;
        };
        if (sort.equals("id")) {
            return categoryRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "id")));
        }
        if (sort.equals("name")) {
            return categoryRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "name")));
        }
        if (sort.equals("createdAt")) {
            return categoryRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "createdAt")));
        }
        PageRequest pageRequest = PageRequest.of(page, perPage, Sort.by(direction, sort));
        return categoryRepository.findAll(specification, pageRequest);
    }

    @Override
    public List<Category> getMainCategories() {
        return categoryRepository.findByParentIdIsNull();
    }

    @Override
    public List<Category> getSubCategories(Integer parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    @Override
    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(customUserDetails.getUsername());
        if (user.isPresent()) {
            User currentUser = user.get();
            boolean existedCategory = categoryRepository.existsByNameAndParentId(category.getName(), category.getParentId());
            if (!existedCategory) {
                Category newCategory = new Category();
                newCategory.setName(category.getName());
                newCategory.setParentId(category.getParentId());
                newCategory.setCreatedBy(currentUser);
                newCategory.setCreatedAt(getCurrentTimeInVietnam());
                return categoryRepository.save(newCategory);
            } else {
                System.out.println("Không thể tạo danh mục mới");
            }
        } else {
            System.out.println("Người dùng không tồn tại");
        }
        return null;
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
