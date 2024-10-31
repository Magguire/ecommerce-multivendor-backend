package com.eshop.service;

import com.eshop.model.Order;
import com.eshop.model.PaymentOrder;
import com.eshop.model.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService {

    PaymentOrder createPaymentOrder(User user, Set<Order> orders);
    PaymentOrder findPaymentOrderById(Long Id) throws Exception;
    PaymentOrder findPaymentOrderByPaymentLinkId(String paymentLinkId) throws Exception;
    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;
    PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException;
    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;

}
