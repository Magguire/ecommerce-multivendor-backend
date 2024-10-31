package com.eshop.repository;

import com.eshop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);
}
