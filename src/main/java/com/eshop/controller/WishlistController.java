package com.eshop.controller;

import com.eshop.model.Product;
import com.eshop.model.User;
import com.eshop.model.Wishlist;
import com.eshop.service.ProductService;
import com.eshop.service.UserService;
import com.eshop.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final UserService userService;
    private final WishlistService wishlistService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Wishlist> getWishlistByUserId(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);

        Wishlist wishlist = this.wishlistService.getWishlistByUserId(user);

        return new ResponseEntity<Wishlist>(wishlist, HttpStatus.OK);

    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(@RequestHeader("Authorization") String jwt,
                                                         @PathVariable Long productId) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);
        Product product = this.productService.findProductById(productId);
        Wishlist wishlist = this.wishlistService.addProductToWishlist(user, product);

        return ResponseEntity.ok(wishlist);

    }
}
