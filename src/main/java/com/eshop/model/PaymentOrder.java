package com.eshop.model;


import com.eshop.domain.PAYEMENT_ORDER_STATUS;
import com.eshop.domain.PAYMENT_METHOD;
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
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Generate Id automatically
    private Long id;

    private Long amount;

    private PAYEMENT_ORDER_STATUS paymentOrderStatus = PAYEMENT_ORDER_STATUS.PENDING;

    private PAYMENT_METHOD paymentMethod;

    private String paymentLinkId;

    @ManyToOne // One user can have multiple payment orders
    private User user;

   @OneToMany  // One payment order can be for several orders
   private Set<Order> orders = new HashSet<>();
}
