package vn.edu.hcmuaf.fit.websubject.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_money")
    private int totalMoney;

    public OrderDetail(Order order, Product product, int quantity, int totalMoney) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.totalMoney = totalMoney;
    }
}
