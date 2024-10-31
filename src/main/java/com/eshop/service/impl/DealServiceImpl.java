package com.eshop.service.impl;

import com.eshop.model.Deal;
import com.eshop.model.HomeCategory;
import com.eshop.repository.DealRepository;
import com.eshop.repository.HomeCategoryRepository;
import com.eshop.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public List<Deal> getdeals() {

        return this.dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {

        HomeCategory homeCategory = this.homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);

        Deal newDeal = new Deal();
        newDeal.setCategory(homeCategory);
        newDeal.setDiscount(deal.getDiscount());

        return this.dealRepository.save(deal);
    }

    @Override
    public Deal updateDeal(Long id, Deal deal) throws Exception {

        HomeCategory category = this.homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);

        Deal existingDeal = this.findDealById(id);

        if (deal.getDiscount() != null) {

            existingDeal.setDiscount(deal.getDiscount());
        }

        if (category != null) {

            existingDeal.setCategory(category);

        }

        return this.dealRepository.save(existingDeal);
    }

    @Override
    public void deleteDeal(Long id) throws Exception {

        Deal deal = this.findDealById(id);

        this.dealRepository.delete(deal);

    }

    @Override
    public Deal findDealById(Long id) throws Exception {

        return this.dealRepository.findById(id)
                .orElseThrow(() -> new Exception("Deal does not exiat with id - " + id));
    }
}
