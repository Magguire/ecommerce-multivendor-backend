package com.eshop.model;

import com.eshop.domain.ORDER_STATUS;
import com.eshop.domain.PAYMENT_STATUS;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "orders") // Provide a different table name since Order is a keyword in SQL
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Generate Id automatically
    private Long id;

    private String orderId;

    @ManyToOne
    private User user;

    private Long sellerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne // Multiple orders can be shipped to one address
    private Address shippingAddress;

    @Embedded
    private PaymentDetails paymentDetails = new PaymentDetails();

    private int totalMrpPrice;

    private int totalSellingPrice;

    private double discount;

    private ORDER_STATUS orderStatus = ORDER_STATUS.PENDING;

    private int totalItems;

    private PAYMENT_STATUS paymentStatus=PAYMENT_STATUS.PENDING;

    private LocalDateTime orderDate = LocalDateTime.now();

    private LocalDateTime deliveryDate = orderDate.plusDays(7);




}
