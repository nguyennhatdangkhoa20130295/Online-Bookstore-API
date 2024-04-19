package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.FavoriteProduct;
import vn.edu.hcmuaf.fit.websubject.entity.Product;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.repository.FavoriteRepository;
import vn.edu.hcmuaf.fit.websubject.repository.ProductRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.FavoriteProductService;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteProductServiceImpl implements FavoriteProductService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<FavoriteProduct> getAllFavoriteProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(customUserDetails.getUsername());
        return favoriteRepository.findAllByUserId(user.get().getId());
    }

    @Override
    public FavoriteProduct addFavorite(Integer productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(customUserDetails.getUsername());
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        FavoriteProduct existFavorite = favoriteRepository.findByProductId(productId);
        if (existFavorite != null) {
            return null;
        } else {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isEmpty()) {
                throw new RuntimeException("Product not found");
            }
            Product product = productOptional.get();
            FavoriteProduct favoriteProduct = new FavoriteProduct();
            favoriteProduct.setProduct(product);
            favoriteProduct.setUser(user.get());
            return favoriteRepository.save(favoriteProduct);
        }
    }

    @Override
    public void deleteFavorite(Integer id) {
        favoriteRepository.deleteById(id);
    }
}
