package com.eshop.repository;

import com.eshop.domain.ACCOUNT_STATUS;
import com.eshop.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(ACCOUNT_STATUS status);
}
