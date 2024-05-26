package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.entity.CartItems;

import java.util.List;

public interface CartItemsService {
    void addToCart(CartItems cartItems);

    void removeFromCart(int cartItemId);

    List<CartItems> getCartItems();

    void increaseCartItemQuantity(int cartItemId);

    void decreaseCartItemQuantity(int cartItemId);
}
