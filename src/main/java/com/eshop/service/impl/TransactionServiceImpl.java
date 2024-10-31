package com.eshop.service.impl;

import com.eshop.model.Order;
import com.eshop.model.Seller;
import com.eshop.model.Transaction;
import com.eshop.repository.SellerRepository;
import com.eshop.repository.TransactionRepository;
import com.eshop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Order order) throws Exception {

        Seller seller = this.sellerRepository.findById(order.getSellerId())
                        .orElseThrow(() -> new Exception("Seller not found with id - " + order.getSellerId()));

        Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);

        this.transactionRepository.save(transaction);

        return null;
    }

    @Override
    public List<Transaction> findTransactionsBySellerId(Seller seller) {

        return this.transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return this.transactionRepository.findAll();
    }
}
