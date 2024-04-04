package vn.edu.hcmuaf.fit.websubject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "blog_category")
public class BlogCate {
    @Id
    private int id;
    private String name;

}
