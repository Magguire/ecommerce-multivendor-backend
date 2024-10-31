package com.eshop.service;

import com.eshop.model.Cart;
import com.eshop.model.CartItem;
import com.eshop.model.Product;
import com.eshop.model.User;
import org.springframework.stereotype.Service;



public interface CartService {

    CartItem addCartItem(
            User user,
            Product product,
            String size,
            int quantity
    ) throws Exception;

    Cart findUserCart(User user) throws Exception;
}
