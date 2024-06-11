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
import vn.edu.hcmuaf.fit.websubject.entity.*;
import vn.edu.hcmuaf.fit.websubject.payload.others.CurrentTime;
import vn.edu.hcmuaf.fit.websubject.repository.*;
import vn.edu.hcmuaf.fit.websubject.service.ProductService;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.apache.log4j.Logger;
@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger Log =  Logger.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    private static final String PREFIX = "978"; // Prefix cố định của SKU
    private static final int TOTAL_LENGTH = 13; // Độ dài tổng cộng của SKU
    private static final Random RANDOM = new SecureRandom();

    public static String generateRandomSKU() {
        StringBuilder skuBuilder = new StringBuilder(PREFIX);

        int remainingLength = TOTAL_LENGTH - PREFIX.length();

        for (int i = 0; i < remainingLength; i++) {
            int digit = RANDOM.nextInt(10);
            skuBuilder.append(digit);
        }

        return skuBuilder.toString();
    }

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
            if (discountPrice == product.getOldPrice()) {
                product.setCurrentPrice(product.getOldPrice());
            } else {
                product.setCurrentPrice(discountPrice);
            }
            productRepository.save(product);
        }
    }


    @Override
    public Page<Product> getProductsByCategory(Integer categoryId, int page, int perPage, String sort, String filter, String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC")) {
            direction = Sort.Direction.DESC;
        }

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
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
                        e.printStackTrace();
                    }
                } else if (prices.length == 1) {
                    try {
                        double minPrice = Double.parseDouble(prices[0]);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("currentPrice"), minPrice));
                    } catch (NumberFormatException e) {
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
                return productRepository.findAllByActiveIsTrue(specification, PageRequest.of(page, perPage, Sort.by(direction, "title")));
            }
            case "price-asc", "price-desc" -> {
                return productRepository.findAllByActiveIsTrue(specification, PageRequest.of(page, perPage, Sort.by(direction, "currentPrice")));
            }
            case "latest" -> {
                return productRepository.findAllByActiveIsTrue(specification, PageRequest.of(page, perPage, Sort.by(direction, "id")));
            }
        }

        PageRequest pageRequest = PageRequest.of(page, perPage, Sort.by(direction, sort));
        return productRepository.findAllByActiveIsTrue(specification, pageRequest);
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

    @Override
    public List<Product> getTopSellingProducts() {
        return List.of();
    }

    @Override
    public Product createProduct(Product product) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
            Optional<User> userOptional = userRepository.findByUsername(customUserDetails.getUsername());
            if (userOptional.isEmpty()) {
                Log.warn("Người dùng " + customUserDetails.getUsername() + " không tồn tại");
                throw new RuntimeException("User not found");
            }
            User user = userOptional.get();

            product.setCategory(product.getCategory());
            product.setTitle(product.getTitle());
            product.setImage(product.getImage());
            product.setOldPrice(product.getOldPrice());
            product.setCurrentPrice(product.getCurrentPrice());
            product.setActive(product.isActive());
            product.setCreatedAt(CurrentTime.getCurrentTimeInVietnam());
            product.setCreatedBy(user);
            product.setUpdatedAt(CurrentTime.getCurrentTimeInVietnam());
            product.setUpdatedBy(user);

            ProductDetail detail = new ProductDetail();
            detail.setProduct(product);
            detail.setProductSku(generateRandomSKU());
            detail.setSupplier(product.getDetail().getSupplier());
            detail.setPublisher(product.getDetail().getPublisher());
            detail.setPublishYear(product.getDetail().getPublishYear());
            detail.setAuthor(product.getDetail().getAuthor());
            detail.setBrand(product.getDetail().getBrand());
            detail.setOrigin(product.getDetail().getOrigin());
            detail.setColor(product.getDetail().getColor());
            detail.setWeight(product.getDetail().getWeight());
            detail.setSize(product.getDetail().getSize());
            detail.setQuantityOfPage(product.getDetail().getQuantityOfPage());
            detail.setDescription(product.getDetail().getDescription());
            productDetailRepository.save(detail);

            product.setDetail(detail);


            if (product.getImages() == null) {
                product.setImages(new ArrayList<>());
            }

            List<ProductImage> productImages = new ArrayList<>();

            ProductImage mainImage = new ProductImage();
            mainImage.setProduct(product);
            mainImage.setImage(product.getImage());
            mainImage.setCreatedAt(CurrentTime.getCurrentTimeInVietnam());
            mainImage.setUpdatedAt(CurrentTime.getCurrentTimeInVietnam());
            mainImage.setDeleted(false);
            productImages.add(mainImage);

            for (ProductImage productImage : product.getImages()) {
                productImage.setProduct(product);
                productImage.setImage(productImage.getImage());
                productImage.setCreatedAt(CurrentTime.getCurrentTimeInVietnam());
                productImage.setUpdatedAt(CurrentTime.getCurrentTimeInVietnam());
                productImage.setDeleted(false);
            }
            product.setImages(productImages);

            Log.info("Sản phẩm " + product.getTitle() + " được tạo bởi " + user.getUsername());
            return product;
        } catch (Exception e) {
            Log.error("Lỗi khi tạo sản phẩm:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
