package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.CartItem;
import vn.edu.hcmuaf.fit.websubject.entity.Log;
import vn.edu.hcmuaf.fit.websubject.entity.Product;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.repository.CartItemsRepository;
import vn.edu.hcmuaf.fit.websubject.repository.ProductRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.CartItemsService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemsServiceImpl implements CartItemsService {
    private static final Logger Log =  Logger.getLogger(CartItemsServiceImpl.class);
    @Autowired
    CartItemsRepository cartItemsRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    public void addToCart(CartItem cartItems) {
        try {
            CartItem existingCartItem = cartItemsRepository.findByProductId(cartItems.getProduct().getId());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
            Optional<User> user = userRepository.findByUsername(customUserDetails.getUsername());
            if (user.isPresent()) {
                User currentUser = user.get();
                if (existingCartItem != null) {
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItems.getQuantity());
                    cartItemsRepository.save(existingCartItem);
                } else {
                    Optional<Product> productOptional = productRepository.findById(cartItems.getProduct().getId());
                    if (productOptional.isPresent()) {
                        Product product = productOptional.get();
                        CartItem cartItem = new CartItem();
                        cartItem.setProduct(product);
                        cartItem.setQuantity(cartItems.getQuantity());
                        cartItem.setUser(currentUser);
                        cartItemsRepository.save(cartItem);
                        Log.info(currentUser.getUserInfo().getFullName() +" đã thêm "+cartItems.getProduct().getTitle()+" vào giỏ hàng");
                    } else {
                        Log.warn("Không tim thấy sản phẩm "+cartItems.getProduct().getTitle());
                        System.out.println("Không tim thấy sản phẩm");
                    }
                }
            }
        } catch (Exception e) {
            Log.error("Lỗi khi thêm "+cartItems.getProduct().getTitle()+" vào giỏ hàng");
            System.out.println("Lỗi khi thêm vào giỏ hàng");
        }
    }

    public void removeFromCart(int cartItemId) {
        try {
            cartItemsRepository.deleteById(cartItemId);
            Log.info("Đã xóa sản phẩm với id #"+cartItemId+" khỏi giỏ hàng");
        } catch (Exception e) {
            Log.error("Lỗi khi xóa sản phẩm với id #"+cartItemId+" khỏi giỏ hàng");
            System.out.println("Lỗi khi xóa sản phẩm khỏi giỏ hàng");
        }
    }

    public List<CartItem> getCartItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(customUserDetails.getUsername());
        return cartItemsRepository.findAllByUserId(user.get().getId());
    }

    @Override
    public void increaseCartItemQuantity(int cartItemId) {
        Optional<CartItem> cartItemOptional = cartItemsRepository.findById(cartItemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            int newQuantity = cartItem.getQuantity() + 1;
            cartItem.setQuantity(newQuantity);
            cartItemsRepository.save(cartItem);
        }
    }

    public void decreaseCartItemQuantity(int cartItemId) {
        Optional<CartItem> cartItemOptional = cartItemsRepository.findById(cartItemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
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
