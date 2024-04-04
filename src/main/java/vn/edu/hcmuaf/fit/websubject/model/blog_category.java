package vn.edu.hcmuaf.fit.websubject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "blog_category")
public class blog_category {
    @Id
    @JoinColumn(name = "id")
    private int id;
    @JoinColumn(name = "name")
    private String name;

}
