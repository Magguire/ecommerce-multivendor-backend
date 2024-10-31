package com.eshop.service;

import com.eshop.model.Seller;
import com.eshop.model.SellerReport;

public interface SellerReportService {

    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);

}

