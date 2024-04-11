package vn.edu.hcmuaf.fit.websubject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "store_id")
    private int storeId;

    @Column(name = "shipping_address")
    private int shipAddress;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "order_total")
    private int orderTotal;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "payment_method")
    private String paymentMethod;

    @OneToOne
    @JoinColumn(name = "status", referencedColumnName = "id", nullable = false)
    private OrderStatus status;

    @Column(name = "shipping_cost")
    private int shippingCost;

}
