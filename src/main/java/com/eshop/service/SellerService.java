package com.eshop.service;

import com.eshop.domain.ACCOUNT_STATUS;
import com.eshop.exceptions.SellerException;
import com.eshop.model.Seller;

import java.util.List;

public interface SellerService {

    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller> getAllSellers(ACCOUNT_STATUS status);
    Seller updateSeller(Long id, Seller seller) throws Exception;
    void deleteSeller(Long id) throws Exception;
    Seller verifySeller(String email, String otp) throws Exception;
    Seller updateSellerAccountStatus(Long id, ACCOUNT_STATUS status) throws Exception;

}
