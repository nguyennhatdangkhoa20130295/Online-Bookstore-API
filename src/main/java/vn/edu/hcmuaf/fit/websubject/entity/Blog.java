package vn.edu.hcmuaf.fit.websubject.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "blog_cate_id")
    private BlogCategory blog_cate_id;
    private String image;
    @OneToOne
    @JoinColumn(name = "creator")
    private User creator;
    private String title;
    private String content;
    private String created_at;

}
