package com.eshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Generate Id automatically
    private Long id;

    @OneToOne // One wishlist belongs to only one user
    private User user;

    @ManyToMany // Multiple products can belong to multiple wishlists
    private Set<Product> products = new HashSet<>();


}
