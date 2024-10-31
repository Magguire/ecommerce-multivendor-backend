package com.eshop.repository;

import com.eshop.model.Cart;
import com.eshop.model.CartItem;
import com.eshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
