package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.websubject.entity.CartItems;
import vn.edu.hcmuaf.fit.websubject.service.CartItemsService;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartItemsService cartItemsService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartItems cartItems) {
        cartItemsService.addToCart(cartItems);
        return ResponseEntity.ok("Added to cart successfully");
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable int cartItemId) {
        cartItemsService.removeFromCart(cartItemId);
        return ResponseEntity.ok("Removed from cart successfully");
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItems>> getCartItems() {
        List<CartItems> cartItems = cartItemsService.getCartItems();
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("/increase/{cartItemId}")
    public ResponseEntity<String> increaseCartItemQuantity(@PathVariable int cartItemId) {
        cartItemsService.increaseCartItemQuantity(cartItemId);
        return ResponseEntity.ok("Cart item quantity increased successfully");
    }

    @PutMapping("/decrease/{cartItemId}")
    public ResponseEntity<String> decreaseCartItemQuantity(@PathVariable int cartItemId) {
        cartItemsService.decreaseCartItemQuantity(cartItemId);
        return ResponseEntity.ok("Cart item quantity decreased successfully");
    }
}
