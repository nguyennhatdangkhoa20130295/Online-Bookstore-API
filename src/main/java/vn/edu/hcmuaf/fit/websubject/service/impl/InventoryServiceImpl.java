package vn.edu.hcmuaf.fit.websubject.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Inventory;
import vn.edu.hcmuaf.fit.websubject.entity.Product;
import vn.edu.hcmuaf.fit.websubject.payload.others.CurrentTime;
import vn.edu.hcmuaf.fit.websubject.payload.request.InventoryRequest;
import vn.edu.hcmuaf.fit.websubject.repository.InventoryRepository;
import vn.edu.hcmuaf.fit.websubject.repository.ProductRepository;
import vn.edu.hcmuaf.fit.websubject.service.InventoryService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Optional<Inventory> getByProductId(int productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @Override
    public Page<Inventory> getAllInventories(int page, int perPage, String sort, String filter, String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC"))
            direction = Sort.Direction.DESC;

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Specification<Inventory> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("q")) {
                String searchStr = filterJson.get("q").asText().toLowerCase();
                Join<Inventory, Product> productJoin = root.join("product", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("title")), "%" + searchStr + "%"));
            }
            if (filterJson.has("active")) {
                Boolean active = Boolean.valueOf(filterJson.get("active").asText());
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("active"), active));
            }
            return predicate;
        };
        PageRequest pageRequest = PageRequest.of(page, perPage, Sort.by(direction, sort));
        return inventoryRepository.findAll(specification, pageRequest);
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        inventory.setCreatedAt(CurrentTime.getCurrentTimeInVietnam());
        return inventoryRepository.save(inventory);

    }

    @Override
    public List<Inventory> createInventories(List<InventoryRequest> inventoryRequests) {
        List<Inventory> inventories = new ArrayList<>();
        for (InventoryRequest inventoryRequest : inventoryRequests) {
            Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(inventoryRequest.getProductId());
            Optional<Product> productOptional = productRepository.findById(inventoryRequest.getProductId());
            if (productOptional.isEmpty()) {
                throw new RuntimeException("Product not found");
            }
            Product product = productOptional.get();
            Inventory inventory = new Inventory();
            if (inventoryOptional.isEmpty()) {
                inventory.setProduct(product);
                inventory.setImportPrice(inventoryRequest.getImportPrice());
                inventory.setSalePrice(inventoryRequest.getSalePrice());
                inventory.setImportedQuantity(inventoryRequest.getQuantity());
                inventory.setRemainingQuantity(inventoryRequest.getQuantity());
                inventory.setCreatedAt(CurrentTime.getCurrentTimeInVietnam());
                inventory.setActive(true);
                inventoryRepository.save(inventory);
                inventories.add(inventory);
                product.setOldPrice(inventoryRequest.getSalePrice());
                product.setCurrentPrice(inventoryRequest.getSalePrice());
                product.setActive(true);
                productRepository.save(product);
            }
        }
        return inventories;
    }


}
