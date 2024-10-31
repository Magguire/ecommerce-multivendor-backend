package com.eshop.controller;

import com.eshop.model.*;
import com.eshop.response.ApiResponse;
import com.eshop.response.PaymentLinkResponse;
import com.eshop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final SellerService sellerService;
    private final UserService userService;
    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(@RequestHeader("Authorization") String jwt,
                                                             @PathVariable String paymentId,
                                                             @RequestParam String paymentLinkId) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        PaymentOrder paymentOrder = this.paymentService.findPaymentOrderByPaymentLinkId(paymentLinkId);

        boolean paymentSuccess = this.paymentService.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);

        if (paymentSuccess) {

            for (Order order: paymentOrder.getOrders()) {

               transactionService.createTransaction(order);

                Seller seller = this.sellerService.getSellerById(order.getSellerId());

                SellerReport sellerReport = this.sellerReportService.getSellerReport(seller);
                sellerReport.setTotalOrders(sellerReport.getTotalOrders()+1);
                sellerReport.setTotalEarnings(sellerReport.getTotalEarnings() + order.getTotalSellingPrice());
                sellerReport.setTotalSales(sellerReport.getTotalSales()+order.getOrderItems().size());

                this.sellerReportService.updateSellerReport(sellerReport);

            }

            ApiResponse response = new ApiResponse();
            response.setMessage("Payment Success");

            return new ResponseEntity<ApiResponse>(response, HttpStatus.CREATED);
        }

        ApiResponse response = new ApiResponse();
        response.setMessage("Payment Failed");

        return new ResponseEntity<ApiResponse>(response, HttpStatus.CREATED);


    }

}
