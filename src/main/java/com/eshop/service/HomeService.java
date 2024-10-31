package com.eshop.service;

import com.eshop.model.Home;
import com.eshop.model.HomeCategory;

import java.util.List;

public interface HomeService {

    Home createHomePageData(List<HomeCategory> allHomeCategories);

}
