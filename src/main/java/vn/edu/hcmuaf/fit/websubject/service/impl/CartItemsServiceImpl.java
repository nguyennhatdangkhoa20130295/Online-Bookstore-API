package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.CartItems;
import vn.edu.hcmuaf.fit.websubject.entity.Product;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.repository.CartItemsRepository;
import vn.edu.hcmuaf.fit.websubject.repository.ProductRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.CartItemsService;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemsServiceImpl implements CartItemsService {
    @Autowired
    CartItemsRepository cartItemsRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    public void addToCart(CartItems cartItems) {
        CartItems existingCartItem = cartItemsRepository.findByProductId(cartItems.getProduct().getId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(customUserDetails.getUsername());
        if (user.isPresent()) {
            User currentUser = user.get();
            if (existingCartItem != null) {
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                cartItemsRepository.save(existingCartItem);
            } else {
                Optional<Product> productOptional = productRepository.findById(cartItems.getProduct().getId());
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    CartItems cartItem = new CartItems();
                    cartItem.setProduct(product);
                    cartItem.setQuantity(cartItems.getQuantity());
                    cartItem.setUser(currentUser);
                    cartItemsRepository.save(cartItem);
                } else {
                    System.out.println("Không tim thấy sản phẩm");
                }
            }
        }
    }

    public void removeFromCart(int cartItemId) {
        cartItemsRepository.deleteById(cartItemId);
    }

    public List<CartItems> getCartItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(customUserDetails.getUsername());
        return cartItemsRepository.findAllByUserId(user.get().getId());
    }

    @Override
    public void increaseCartItemQuantity(int cartItemId) {
        Optional<CartItems> cartItemOptional = cartItemsRepository.findById(cartItemId);
        if (cartItemOptional.isPresent()) {
            CartItems cartItem = cartItemOptional.get();
            int newQuantity = cartItem.getQuantity() + 1;
            cartItem.setQuantity(newQuantity);
            cartItemsRepository.save(cartItem);
        }
    }

    public void decreaseCartItemQuantity(int cartItemId) {
        Optional<CartItems> cartItemOptional = cartItemsRepository.findById(cartItemId);
        if (cartItemOptional.isPresent()) {
            CartItems cartItem = cartItemOptional.get();
            int newQuantity = cartItem.getQuantity() - 1;
            if (newQuantity <= 0) {
                System.out.println("không thể giảm thêm số lượng");
            } else {
                cartItem.setQuantity(newQuantity);
                cartItemsRepository.save(cartItem);
            }
        }
    }
}
