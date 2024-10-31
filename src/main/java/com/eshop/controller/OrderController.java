package com.eshop.controller;

import com.eshop.domain.PAYMENT_METHOD;
import com.eshop.model.*;
import com.eshop.repository.PaymentOrderRepository;
import com.eshop.response.ApiResponse;
import com.eshop.response.PaymentLinkResponse;
import com.eshop.service.*;
import com.razorpay.PaymentLink;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final SellerService sellerService;
    private final UserService userService;
    private final CartService cartService;
    private final SellerReportService sellerReportService;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;


    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(@RequestHeader("Authorization") String jwt,
                                                                  @RequestBody Address shippingAddress,
                                                                  @RequestParam PAYMENT_METHOD paymentMethod) throws Exception {


        User user = this.userService.findUserByJwtToken(jwt);

        Cart cart = this.cartService.findUserCart(user);

        Set<Order> orders = this.orderService.createOrder(user, shippingAddress, cart);

        PaymentOrder paymentOrder = this.paymentService.createPaymentOrder(user, orders);

        PaymentLinkResponse response = new PaymentLinkResponse();

        // Create razorpay payment link

        if (paymentMethod.equals(PAYMENT_METHOD.RAZORPAY)) {

            PaymentLink paymentLink = this.paymentService.createRazorpayPaymentLink(user,
                    paymentOrder.getAmount(), paymentOrder.getId());

            String paymentUrl = paymentLink.get("short_url");
            String paymentLinkId = paymentLink.get("id");


            response.setPayment_link_url(paymentUrl);
            response.setPayment_link_id(paymentLinkId);

            // Save payment link Id
            paymentOrder.setPaymentLinkId(paymentLinkId);
            this.paymentOrderRepository.save(paymentOrder);

        } else { // Create stripe payment link

            String paymentUrl = this.paymentService.createStripePaymentLink(user,
                    paymentOrder.getAmount(), paymentOrder.getId());

            response.setPayment_link_url(paymentUrl);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistoryHandler(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);

        List<Order> orders = this.orderService.usersOrderHistory(user.getId());

        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId,
                                              @RequestHeader("Authorization") String jwt) throws Exception {


        User user = this.userService.findUserByJwtToken(jwt);

        Order order = this.orderService.findOrderById(orderId);

        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(@RequestHeader("Authorization") String jwt,
                                                      @PathVariable Long orderItemId) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);

        OrderItem orderItem = this.orderService.findOrderItemById(orderItemId);

        return new ResponseEntity<OrderItem>(orderItem, HttpStatus.OK);
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<Order> cancelOrderHandler(@RequestHeader("Authorization") String jwt,
                                                    @PathVariable Long orderId) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);

        Order order = this.orderService.cancelOrder(orderId, user);

        /* Add logic here to update seller report */
        Seller seller = this.sellerService.getSellerById(order.getSellerId());

        SellerReport sellerReport = this.sellerReportService.getSellerReport(seller);

        sellerReport.setCancelledOrders(sellerReport.getCancelledOrders()+1);

        sellerReport.setTotalRefunds(sellerReport.getTotalRefunds() + (int) order.getTotalSellingPrice());

        SellerReport updatedSellerReport = this.sellerReportService.updateSellerReport(sellerReport);

        return new ResponseEntity<Order>(order, HttpStatus.OK);


    }
}
