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
import vn.edu.hcmuaf.fit.websubject.entity.Product;
import vn.edu.hcmuaf.fit.websubject.entity.Promotion;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.repository.*;
import vn.edu.hcmuaf.fit.websubject.service.ProductService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProducts(int page, int perPage, String sort, String filter, String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC"))
            direction = Sort.Direction.DESC;

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("title")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + filterJson.get("title").asText() + "%"));
            }

            return predicate;
        };
        PageRequest pageRequest = PageRequest.of(page, perPage, Sort.by(direction, sort));
        return productRepository.findAll(specification, pageRequest);
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProductWithPromotion() {
        List<Product> allProducts = productRepository.findAll();
        List<Product> productsWithPromotion = new ArrayList<>();
        for (Product product : allProducts) {
            Optional<Promotion> promotion = promotionRepository.findByProductId(product.getId());
            if (promotion.isPresent()) {
                productsWithPromotion.add(product);
            }
        }
        return productsWithPromotion;
    }

    @Override
    public void setDiscountPrice(int id, int discountPrice) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if(discountPrice==product.getOldPrice()){
                product.setCurrentPrice(product.getOldPrice());
                System.out.println(product);
            } else {
                product.setCurrentPrice(discountPrice);
            }
            productRepository.save(product);
            }
    }


    @Override
    public Page<Product> getProductsByCategory(Integer categoryId, int page, int perPage, String sort, String filter, String order) {
        System.out.println(filter);
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC")) {
            direction = Sort.Direction.DESC;
        }

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
            System.out.println(filterJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            // Lọc theo tiêu đề sản phẩm
            if (filterJson.has("title")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + filterJson.get("title").asText() + "%"));
            }
            if (filterJson.has("currentPrice")) {
                String priceRange = filterJson.get("currentPrice").asText();
                String[] prices = priceRange.split("-");
                if (prices.length == 2) {
                    try {
                        double minPrice = Double.parseDouble(prices[0]);
                        double maxPrice = Double.parseDouble(prices[1]);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("currentPrice"), minPrice, maxPrice));
                    } catch (NumberFormatException e) {
                        // Handle the error if the price values are not valid numbers
                        e.printStackTrace();
                    }
                } else if (prices.length == 1) {
                    // Only one price given, assume it's the minimum price
                    try {
                        double minPrice = Double.parseDouble(prices[0]);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("currentPrice"), minPrice));
                    } catch (NumberFormatException e) {
                        // Handle the error if the price values are not valid numbers
                        e.printStackTrace();
                    }
                }
            }

            // Lọc theo danh mục, danh mục cha và danh mục cha của danh mục cha
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("category").get("id"), categoryId),
                    criteriaBuilder.equal(root.get("category").get("parentCategory").get("id"), categoryId),
                    criteriaBuilder.equal(root.get("category").get("parentCategory").get("parentCategory").get("id"), categoryId)
            ));

            return predicate;
        };
        switch (sort) {
            case "atoz", "ztoa" -> {
                return productRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "title")));
            }
            case "price-asc", "price-desc" -> {
                return productRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "currentPrice")));
            }
            case "latest" -> {
                return productRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "id")));
            }
        }

        PageRequest pageRequest = PageRequest.of(page, perPage, Sort.by(direction, sort));
        return productRepository.findAll(specification, pageRequest);
    }

    @Override
    public List<Product> getThreeLatestProduct() {
        return productRepository.findTop3ByOrderByIdDesc();
    }

    @Override
    public List<Product> getProductsByCategoryId(Integer categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> getFeatureProducts() {
        return productRepository.findRandomProducts();
    }

    @Override
    public List<Product> getTopReviewProducts() {
        return productRepository.findTopReviewProducts();
    }

}
