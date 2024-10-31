package com.eshop.model;

import com.eshop.domain.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode  // This considers records whose values all match to be equal
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Generate Id automatically
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Do not fetch password to front-end
    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String mobile;

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER; // Define default user as customer

    @OneToMany // One user can have multiple addresses but one address belongs to one user
    private Set<Address> addresses = new HashSet<>();  // HashSet is like a set in python. Time complexity = O(1)

    @ManyToMany // Multiple users can use multiple coupons and vice-versa
    @JsonIgnore // Will not be fetched in Frontend
    private Set<Coupon> usedCoupons = new HashSet<>();

}
