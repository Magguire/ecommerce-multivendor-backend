package com.eshop.service.impl;

import com.eshop.model.Seller;
import com.eshop.model.SellerReport;
import com.eshop.repository.SellerReportRepository;
import com.eshop.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;

    @Override
    public SellerReport getSellerReport(Seller seller) {

        SellerReport sellerReport = this.sellerReportRepository.findBySellerId(seller.getId());

        if (sellerReport == null) {

            // Create new seller report

            SellerReport newReport = new SellerReport();
            newReport.setSeller(seller);

            return this.sellerReportRepository.save(newReport);

        }

        return sellerReport;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {

        return this.sellerReportRepository.save(sellerReport);
    }
}
