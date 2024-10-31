package com.eshop.service;

import com.eshop.model.HomeCategory;

import java.util.List;

public interface HomeCategoryService {

    HomeCategory createHomeCategory(HomeCategory homeCategory);
    List<HomeCategory> createHomeCategories(List<HomeCategory> homeCategories) throws Exception;
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception;
    List<HomeCategory> getAllHomeCategories();
    HomeCategory findHomeCategoryById(Long id) throws Exception;
}
