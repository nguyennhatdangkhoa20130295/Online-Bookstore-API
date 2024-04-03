package vn.edu.hcmuaf.fit.websubject.model;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EnumRole description;

    public Roles() {

    }

    public Roles(EnumRole name) {
        this.description = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EnumRole getDescription() {
        return description;
    }

    public void setDescription(EnumRole name) {
        this.description = name;
    }
}
