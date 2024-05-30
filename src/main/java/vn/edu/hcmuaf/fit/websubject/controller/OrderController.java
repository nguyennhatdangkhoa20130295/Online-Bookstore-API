package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.websubject.entity.CartItems;
import vn.edu.hcmuaf.fit.websubject.entity.Order;
import vn.edu.hcmuaf.fit.websubject.entity.OrderDetail;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.service.CartItemsService;
import vn.edu.hcmuaf.fit.websubject.service.OrderService;
import vn.edu.hcmuaf.fit.websubject.service.UserService;
import vn.edu.hcmuaf.fit.websubject.service.impl.CustomUserDetailsImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartItemsService cartItemsService;

    @GetMapping
    public ResponseEntity<?> getUserOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userService.getUserByUsername(customUserDetails.getUsername());
        if (user.isPresent()) {
            List<Order> orders = orderService.getUserOrders(user.get().getId());
            return ResponseEntity.ok().body(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        Order newOrder = orderService.createOrder(order);
        List<CartItems> cartItems = cartItemsService.getCartItems();
        Order latestOrder = orderService.getLatestOrder(newOrder.getUser().getId());
        for (CartItems cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail(latestOrder, cartItem.getProduct(), cartItem.getQuantity(), cartItem.getQuantity() * cartItem.getProduct().getCurrentPrice());
            orderService.createOrderDetail(orderDetail);
            cartItemsService.removeFromCart(cartItem.getId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @GetMapping("/product/{idProduct}/user/{userId}")
    public ResponseEntity<OrderDetail> getOrderByProductId(@PathVariable Integer idProduct, @PathVariable Integer userId) {
        Optional<OrderDetail> orderDetail = orderService.getOrderByProductIdAndUserId(idProduct, userId);
        if (orderDetail.isPresent()) {
            OrderDetail order = orderDetail.get();
            return ResponseEntity.ok().body(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Integer userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId) {
        Optional<Order> orderOptional = orderService.getOrder(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return ResponseEntity.ok().body(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
