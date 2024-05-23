package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.websubject.entity.Product;
import vn.edu.hcmuaf.fit.websubject.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "24") int perPage,
                                                        @RequestParam(defaultValue = "id") String sort,
                                                        @RequestParam(defaultValue = "{}") String filter,
                                                        @RequestParam(defaultValue = "DESC") String order) {
        Page<Product> products = productService.getAllProducts(page, perPage, sort, filter, order);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return ResponseEntity.ok().body(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Product>> getProductsByCategory(@PathVariable Integer categoryId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "24") int perPage,
                                                               @RequestParam(defaultValue = "id") String sort,
                                                               @RequestParam(defaultValue = "{}") String filter,
                                                               @RequestParam(defaultValue = "ASC") String order) {
        Page<Product> products = productService.getProductsByCategory(categoryId, page, perPage, sort, filter, order);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/latest")
    public List<Product> getThreeLatestProduct() {
        return productService.getThreeLatestProduct();
    }

//    @PostMapping("/add")
//    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
//        Product savedProduct = productService.addProduct(product);
//        return ResponseEntity.ok(savedProduct);
//    }

}
