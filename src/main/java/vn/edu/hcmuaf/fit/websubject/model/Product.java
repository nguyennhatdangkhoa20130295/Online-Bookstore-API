package vn.edu.hcmuaf.fit.websubject.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "old_price")
    private Long oldPrice;

    @Column(name = "current_price")
    private Long currentPrice;

    @Column(name = "is_on_sale")
    private boolean isOnSale;
}
