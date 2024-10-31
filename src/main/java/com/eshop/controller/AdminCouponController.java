package com.eshop.controller;

import com.eshop.model.Cart;
import com.eshop.model.Coupon;
import com.eshop.model.User;
import com.eshop.response.ApiResponse;
import com.eshop.service.CartService;
import com.eshop.service.CouponService;
import com.eshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
public class AdminCouponController {

    private final CouponService couponService;
    private final UserService userService;
    private final CartService cartService;

    @PostMapping("/apply") // Add or remove coupon
    public ResponseEntity<Cart> applyCoupon(@RequestHeader("Authorization") String jwt,
                                            @RequestParam String apply, // boolean as string
                                            @RequestParam String code,
                                            @RequestParam double orderValue) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);

        Cart cart;

        if (apply.equals("true")) {

            cart = this.couponService.applyCoupon(code, orderValue, user);

        } else {

            cart = this.couponService.removeCoupon(code, user);
        }

     return ResponseEntity.ok(cart);

    }

    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) throws Exception {

        Coupon newCoupon = this.couponService.createCoupon(coupon);

        return new ResponseEntity<Coupon>(newCoupon, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCouponHandler(@PathVariable Long id) throws Exception {

        this.couponService.deleteCoupon(id);

        ApiResponse response = new ApiResponse();
        response.setMessage("Coupon deleted successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Coupon>> getAllCoupons() {

        List<Coupon> coupons = this.couponService.findAllCoupons();

        return ResponseEntity.ok(coupons);
    }
}
