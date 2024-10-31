package com.eshop.controller;

import com.eshop.domain.ORDER_STATUS;
import com.eshop.model.Order;
import com.eshop.model.Seller;
import com.eshop.service.OrderService;
import com.eshop.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrdersHandler(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = this.sellerService.getSellerProfile(jwt);
        List<Order> orders = this.orderService.sellersOrder(seller.getId());

        return new ResponseEntity<List<Order>>(orders, HttpStatus.ACCEPTED);

    }

    @PatchMapping("/{orderId}/status/{status}")
    public ResponseEntity<Order> updateOrderHandler(@RequestHeader("Authorization") String jwt,
                                                    @PathVariable Long orderId,
                                                    @PathVariable ORDER_STATUS status) throws Exception {

        Order order = this.orderService.updateOrderStatus(orderId, status);

        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
}
