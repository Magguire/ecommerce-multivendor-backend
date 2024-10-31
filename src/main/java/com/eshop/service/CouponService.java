package com.eshop.service;

import com.eshop.model.Cart;
import com.eshop.model.Coupon;
import com.eshop.model.User;

import java.util.List;

public interface CouponService {

    Cart applyCoupon(String couponCode, double orderValue, User user) throws Exception;
    Cart removeCoupon(String couponCode, User user) throws Exception;
    Coupon findCouponById(Long id) throws Exception;
    Coupon createCoupon(Coupon coupon) throws Exception;
    List<Coupon> findAllCoupons();
    void deleteCoupon(Long couponId) throws Exception;
}
