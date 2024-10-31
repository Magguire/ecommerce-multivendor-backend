package com.eshop.service;

import com.eshop.model.Deal;

import java.util.List;

public interface DealService {

    List<Deal> getdeals();
    Deal createDeal(Deal deal);
    Deal updateDeal(Long id, Deal deal) throws Exception;
    void deleteDeal(Long id) throws Exception;
    Deal findDealById(Long id) throws Exception;

}
