package vn.edu.hcmuaf.fit.websubject.controller;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.websubject.entity.*;
import vn.edu.hcmuaf.fit.websubject.service.CartItemsService;
import vn.edu.hcmuaf.fit.websubject.service.InventoryService;
import vn.edu.hcmuaf.fit.websubject.service.OrderService;
import vn.edu.hcmuaf.fit.websubject.service.PromotionService;
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
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PromotionService promotionService;
    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "24") int perPage,
                                                        @RequestParam(defaultValue = "id") String sort,
                                                        @RequestParam(defaultValue = "{}") String filter,
                                                        @RequestParam(defaultValue = "DESC") String order) {
        Page<Order> orders = orderService.getAllOrders(page, perPage, sort, filter, order);
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/user")
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
        List<CartItem> cartItems = cartItemsService.getCartItems();
        for (CartItem cartItem : cartItems) {
            Optional<Inventory> inventoryOptional = inventoryService.getByProductId(cartItem.getProduct().getId());
            if (inventoryOptional.isEmpty()) {
                throw new RuntimeException("Inventory not found");
            }
            Inventory inventory = inventoryOptional.get();
            if (inventory.getRemainingQuantity() < cartItem.getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Không đủ hàng cho sản phẩm: " + cartItem.getProduct().getTitle());
            }
        }
        Order newOrder = orderService.createOrder(order);
        Order latestOrder = orderService.getLatestOrder(newOrder.getUser().getId());
        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail(latestOrder, cartItem.getProduct(), cartItem.getQuantity());
            orderService.createOrderDetail(orderDetail);
            cartItemsService.removeFromCart(cartItem.getId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @GetMapping("/product/{idProduct}/user/{userId}")
    public ResponseEntity<List<Order>> getOrderByProductId(@PathVariable Integer idProduct, @PathVariable Integer userId) {
        List<Order> orders = orderService.getOrderByProductIdAndUserId(idProduct, userId);
        if (orders != null) {
            return ResponseEntity.ok().body(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/promo/{promoCode}/user/{userId}")
    public ResponseEntity<?> getOrderByPromoCode(@PathVariable String promoCode, @PathVariable Integer userId) {
        Order order = orderService.getOrderByPromoCode(promoCode, userId);
        Promotion promotion = promotionService.getPromotionByCode(promoCode);
        if (order != null) {
            return ResponseEntity.ok().body(order);
        } else if(promotion != null) {
            return ResponseEntity.badRequest().body(promotion);
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

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Integer orderId) {
        try {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().body("Đã hủy đơn hàng");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
