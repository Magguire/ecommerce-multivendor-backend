package com.eshop.repository;

import com.eshop.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon findByCode(String couponCode);
}
