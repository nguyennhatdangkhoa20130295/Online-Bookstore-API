package vn.edu.hcmuaf.fit.websubject.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "full_name")
    private String full_name;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "hnum_sname")
    private String hnum_sname;

    @Column(name = "ward_commune")
    private String ward_commune;

    @Column(name = "county_district")
    private String county_district;

    @Column(name = "province_city")
    private String province_city;

    @Column(name = "is_default")
    private boolean is_default;

}
