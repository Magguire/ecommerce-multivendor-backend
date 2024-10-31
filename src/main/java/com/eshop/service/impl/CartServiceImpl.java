package com.eshop.service.impl;

import com.eshop.model.Cart;
import com.eshop.model.CartItem;
import com.eshop.model.Product;
import com.eshop.model.User;
import com.eshop.repository.CartItemRepository;
import com.eshop.repository.CartRepository;
import com.eshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) throws Exception {

        // Check if item quantity is greater than 0
        if (quantity <= 0) {

            throw new Exception("Invalid quantity(0) for cart item");
        }

        Cart cart = this.findUserCart(user);

        CartItem cartItemIsPresent = this.cartItemRepository.findByCartAndProductAndSize(cart, product, size);

        if (cartItemIsPresent == null){

            CartItem cartItem = new CartItem();

            // Calculate total price

            int totalMrpPrice = quantity * product.getMrpPrice();
            int totalSellingPrice = quantity * product.getSellingPrice();
            int totalDiscount = totalMrpPrice - totalSellingPrice;


            cartItem.setMrpPrice(totalMrpPrice);
            cartItem.setSellingPrice(totalSellingPrice);
            cartItem.setSize(size);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());

            // Add item to cart

            cart.getCartItems().add(cartItem);

            cartItem.setCart(cart);

            return this.cartItemRepository.save(cartItem);

           }
//        else{
//
//            cartItemIsPresent.setQuantity(cartItemIsPresent.getQuantity() + 1);
//
//            return this.cartItemRepository.save(cartItemIsPresent);
//
//        }

        return cartItemIsPresent;
    }

    @Override
    public Cart findUserCart(User user) throws Exception {

        Cart cart = this.cartRepository.findByUserId(user.getId());

        if (cart == null) {

            throw new Exception("Cart not found by user id - "+user.getId());
        }

        int totalMrpPrice = 0;
        int totalSellingPrice = 0;
        int totalDiscount = 0;
        int totalItem = 0;


        for (CartItem cartItem: cart.getCartItems()) {

            totalMrpPrice += cartItem.getMrpPrice();
            totalSellingPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();

            // Cart Item is saved with total Mrp Price and selling price. No need to multiply by quantity
            totalDiscount += (cartItem.getMrpPrice() - cartItem.getSellingPrice());
        }

        cart.setTotalMrpPrice(totalMrpPrice);
        cart.setTotalSellingPrice(totalSellingPrice);
        cart.setDiscount(totalDiscount);
        cart.setTotalItem(totalItem);


        return cart;
    }
}
