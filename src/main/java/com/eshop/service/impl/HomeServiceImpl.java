package com.eshop.service.impl;

import com.eshop.domain.HOME_CATEGORY_SECTION;
import com.eshop.model.Deal;
import com.eshop.model.Home;
import com.eshop.model.HomeCategory;
import com.eshop.repository.DealRepository;
import com.eshop.repository.HomeCategoryRepository;
import com.eshop.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeCategoryRepository homeCategoryRepository;
    private final DealRepository dealRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allHomeCategories) {

        // Filter for grid categories.

        List<HomeCategory> gridCategories = allHomeCategories
                .stream().filter(category -> category.getSection() == HOME_CATEGORY_SECTION.GRID)
                .toList();

        // Filter for shop categories

        List<HomeCategory> shopByCategories = allHomeCategories
                .stream().filter(category -> category.getSection() == HOME_CATEGORY_SECTION.SHOP_BY_CATEGORIES)
                .toList();

        // Filter for electronic categories

        List<HomeCategory> electronicsCategories = allHomeCategories
                .stream().filter(category -> category.getSection() == HOME_CATEGORY_SECTION.ELECTRONICS)
                .toList();


        // Filter for deal categories and update deal repo.

        List<Deal> createdDeals;

        List<Deal> allDeals = this.dealRepository.findAll();

        if (allDeals.isEmpty()) {

            // Assume a discount of 10% on all deals

            List<Deal> deals = allHomeCategories
                    .stream().filter(category -> category.getSection() == HOME_CATEGORY_SECTION.DEALS)
                    .map(category -> new Deal(null, 10, category))
                    .toList();

            createdDeals = this.dealRepository.saveAll(deals);

        } else createdDeals = this.dealRepository.findAll();

        // Create home

        Home home = new Home();
        home.setElectronicCategories(electronicsCategories);
        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setDeals(createdDeals);

        return home;
    }
}
