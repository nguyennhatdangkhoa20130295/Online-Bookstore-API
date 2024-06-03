package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.entity.CartItem;

import java.util.List;

public interface CartItemsService {
    void addToCart(CartItem cartItem);

    void removeFromCart(int cartItemId);

    List<CartItem> getCartItems();

    void increaseCartItemQuantity(int cartItemId);

    void decreaseCartItemQuantity(int cartItemId);
}
