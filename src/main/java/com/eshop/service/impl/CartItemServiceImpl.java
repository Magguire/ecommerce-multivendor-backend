package com.eshop.service.impl;

import com.eshop.model.CartItem;
import com.eshop.model.User;
import com.eshop.repository.CartItemRepository;
import com.eshop.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws Exception {

        // Check if item quantity is greater than 0
        if (cartItem.getQuantity() <= 0) {

            throw new Exception("Invalid quantity(0) for cart item");
        }

        // Check if cart item exists

        CartItem cartItemExists = this.findCartItemById(cartItemId);

        User user = cartItemExists.getCart().getUser();

        // Check if user ID matches

        if (user.getId().equals(userId)) {

            // Update price by multiplying new quantity with product unit price

            int totalMrpPrice = cartItem.getQuantity() * cartItemExists.getProduct().getMrpPrice();
            int totalSellingPrice = cartItem.getQuantity() * cartItemExists.getProduct().getSellingPrice();
            //  int totalDiscount = totalMrpPrice - totalSellingPrice;

            // Update quantity
            cartItemExists.setQuantity(cartItem.getQuantity());
            cartItemExists.setMrpPrice(totalMrpPrice);
            cartItemExists.setSellingPrice(totalSellingPrice);

            return cartItemRepository.save(cartItemExists);
        } else{

            throw new Exception("You cannot update another user's cart");
        }

    }

    @Override
    public CartItem findCartItemById(Long id) throws Exception {

        return this.cartItemRepository.findById(id).
                orElseThrow(() -> new Exception("Cart item does not exist with id -" + id));
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {

        CartItem cartItemExists = this.findCartItemById(cartItemId);

        User user = cartItemExists.getCart().getUser();

        if (user.getId().equals(userId)) {

            cartItemRepository.delete(cartItemExists);
        } else{

            throw new Exception("You can't delete another user's item");
        }
    }

}
