package com.eshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // No @EqualsAndHashCode because cart can contain several similar cart items
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne // One user has only one cart
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true) // cascade means update cart when cart items are updated
    private Set<CartItem> cartItems = new HashSet<>();

    private double totalSellingPrice;

    private int totalItem;

    private double totalMrpPrice;

    private double discount;

    private String couponCode;

}
