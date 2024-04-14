package vn.edu.hcmuaf.fit.websubject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "blog_category")
public class BlogCategory {
    @Id
    @JoinColumn(name = "id")
    private int id;
    @JoinColumn(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User created_by;

    @Column(name = "created_at")
    private Date created_at;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updated_by;

    @Column(name = "updated_at")
    private Date updated_at;
}
