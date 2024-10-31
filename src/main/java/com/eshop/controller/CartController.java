package com.eshop.controller;

import com.eshop.model.Cart;
import com.eshop.model.CartItem;
import com.eshop.model.Product;
import com.eshop.model.User;
import com.eshop.request.addCartItemRequest;
import com.eshop.response.ApiResponse;
import com.eshop.service.CartItemService;
import com.eshop.service.CartService;
import com.eshop.service.ProductService;
import com.eshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);

        Cart cart = this.cartService.findUserCart(user);

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestHeader("Authorization") String jwt,
                                                  @RequestBody addCartItemRequest request) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);
        Product product = this.productService.findProductById(request.getProductId());

        CartItem cartItem = this.cartService.addCartItem(user, product, request.getSize(), request.getQuantity());

        ApiResponse response = new ApiResponse();
        response.setMessage("Item added to cart successfully");

        return new ResponseEntity<CartItem>(cartItem, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> removeItemFromCartHandler(@RequestHeader("Authorization") String jwt,
                                                          @PathVariable Long cartItemId) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);

        this.cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse response = new ApiResponse();
        response.setMessage("Cart item has been removed from the cart successfully");

        return new ResponseEntity<ApiResponse>(response, HttpStatus.ACCEPTED);

    }

    @PatchMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(@RequestHeader("Authorization") String jwt,
                                                          @PathVariable Long cartItemId,
                                                          @RequestBody CartItem cartItem) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);

        CartItem updateCartItem = this.cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);

        return new ResponseEntity<CartItem>(updateCartItem, HttpStatus.OK);

    }
}
