package com.eshop.service.impl;

import com.eshop.model.Cart;
import com.eshop.model.Coupon;
import com.eshop.model.User;
import com.eshop.repository.CartRepository;
import com.eshop.repository.CouponRepository;
import com.eshop.repository.UserRepository;
import com.eshop.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public Cart applyCoupon(String couponCode, double orderValue, User user) throws Exception {

        Coupon coupon = this.couponRepository.findByCode(couponCode);

        // check if coupon code exists

        if (coupon == null) {

            throw new Exception("Coupon code is invalid");
        }

        Cart cart = this.cartRepository.findByUserId(user.getId());

        // Check if coupon code has been used by customer

        if (user.getUsedCoupons().contains(coupon)) {

            throw new Exception("Coupon already used");

        }

        // Check if order value is enough to use coupon

        if (orderValue < coupon.getMinimumOrderValue()) {

            throw new Exception("Coupon cannot be used. Order value is less than minimum order value of "
                    + coupon.getMinimumOrderValue());
        }

        // Check if coupon is still active and hasn't expired

        if (coupon.isActive() &&
                LocalDate.now().isAfter(coupon.getValidityStartDate()) &&
        LocalDate.now().isBefore(coupon.getValidityEndDate())) {

            user.getUsedCoupons().add(coupon);
            this.userRepository.save(user);

            // Update user cart
            double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;

            // Update user's price
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
            cart.setCouponCode(couponCode);

            this.cartRepository.save(cart);

            return cart;

        }

        // Return invalid coupon if coupon has expired or is inactive
        else throw new Exception("Coupon is inactive or has expired");

    }

    @Override
    public Cart removeCoupon(String couponCode, User user) throws Exception {

        Coupon coupon = this.couponRepository.findByCode(couponCode);

        // check if coupon code exists

        if (coupon == null) {

            throw new Exception("Coupon code is invalid");

        }

        Cart cart = this.cartRepository.findByUserId(user.getId());

        // Update user cart

        double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;

        // Update user's price
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountedPrice);

        cart.setCouponCode(null);


        return this.cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {

        return this.couponRepository.findById(id)
                .orElseThrow(() -> new Exception("Coupon does not exist with id - "+ id));
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')") // Check if user has admin role
    public Coupon createCoupon(Coupon coupon) throws Exception {

        Coupon couponExists = this.couponRepository.findByCode(coupon.getCode());

        if (couponExists == null){

            return this.couponRepository.save(coupon);


        } else{

            throw new Exception("Coupon already exists");
        }

    }

    @Override
    public List<Coupon> findAllCoupons() {

        return this.couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')") // Check if user has admin role
    public void deleteCoupon(Long couponId) throws Exception {

        Coupon coupon = this.findCouponById(couponId);

        this.couponRepository.delete(coupon);

    }
}
