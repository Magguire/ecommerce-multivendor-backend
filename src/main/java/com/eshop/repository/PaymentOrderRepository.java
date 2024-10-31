package com.eshop.repository;

import com.eshop.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    PaymentOrder findByPaymentLinkId(String PaymentLinkId);
}
