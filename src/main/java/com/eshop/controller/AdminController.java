package com.eshop.controller;

import com.eshop.domain.ACCOUNT_STATUS;
import com.eshop.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshop.model.Seller;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final SellerService sellerService;

    @PatchMapping("/seller/{id}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(@PathVariable Long id,
                                                     @PathVariable ACCOUNT_STATUS status) throws Exception {


        Seller updatedSeller = this.sellerService.updateSellerAccountStatus(id, status);

        return ResponseEntity.ok(updatedSeller);


    }
}
