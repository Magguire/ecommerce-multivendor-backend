package com.eshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Generate Id automatically
    private Long id;

    @ManyToOne // One transaction can only belong to one customer
    private User customer;

    @OneToOne // One transaction is for one order
    private Order order;

    @ManyToOne // One seller can have multiple transaction of orders
    private Seller seller;

    private LocalDateTime date = LocalDateTime.now();
}
