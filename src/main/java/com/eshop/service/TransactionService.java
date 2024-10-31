package com.eshop.service;

import com.eshop.model.Order;
import com.eshop.model.Seller;
import com.eshop.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Order order) throws Exception;
    List<Transaction> findTransactionsBySellerId(Seller seller);
    List<Transaction> getAllTransactions();
}
