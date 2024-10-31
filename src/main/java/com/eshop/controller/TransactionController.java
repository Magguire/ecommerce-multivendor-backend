package com.eshop.controller;

import com.eshop.model.Seller;
import com.eshop.model.Transaction;
import com.eshop.service.SellerService;
import com.eshop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final SellerService sellerService;

    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionBySeller(@RequestHeader("Authorization") String jwt)
            throws Exception {

        Seller seller = this.sellerService.getSellerProfile(jwt);

        List<Transaction> transactions = this.transactionService.findTransactionsBySellerId(seller);

        return new ResponseEntity<List<Transaction>>(transactions,HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() throws Exception {

        List<Transaction> transactions = this.transactionService.getAllTransactions();

        return new ResponseEntity<List<Transaction>>(transactions,HttpStatus.OK);

}
