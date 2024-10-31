package com.eshop.service.impl;

import com.eshop.domain.PAYEMENT_ORDER_STATUS;
import com.eshop.domain.PAYMENT_STATUS;
import com.eshop.model.Order;
import com.eshop.model.PaymentOrder;
import com.eshop.model.User;
import com.eshop.repository.OrderRepository;
import com.eshop.repository.PaymentOrderRepository;
import com.eshop.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${config.param.frontend-url}")
    private String frontendUrl;

    @Value("${config.param.razorpay.api-key}")
    private String razorpayApiKey;

    @Value("${config.param.razorpay.api-secret}")
    private String razorpayApiSecret;

    @Value("${config.param.stripe.api-key}")
    private String stripeApiKey;

    @Value("${config.param.stripe.api-secret}")
    private String stripeApiSecret;

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;


    @Override
    public PaymentOrder createPaymentOrder(User user, Set<Order> orders) {

        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);

        return this.paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder findPaymentOrderById(Long id) throws Exception {

        return this.paymentOrderRepository.findById(id)
                .orElseThrow(() -> new Exception("Payment Order does not exist with Id - " + id));
    }

    @Override
    public PaymentOrder findPaymentOrderByPaymentLinkId(String paymentLinkId) throws Exception {

        PaymentOrder paymentOrder = this.paymentOrderRepository.findByPaymentLinkId(paymentLinkId);

        if (paymentOrder == null){

            throw new Exception("Payment order not found with the provided payment link id - " + paymentLinkId);
        }

        return paymentOrder;
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {

        if (paymentOrder.getPaymentOrderStatus().equals(PAYEMENT_ORDER_STATUS.PENDING)) {

            RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

            Payment payment = razorpayClient.payments.fetch(paymentId);

            String status = payment.get("status");

            if (status.equals("captured")) {

                Set<Order> orders = paymentOrder.getOrders();

                for (Order order:orders) {

                    order.setPaymentStatus(PAYMENT_STATUS.COMPLETED);

                    this.orderRepository.save(order);
                }

                paymentOrder.setPaymentOrderStatus(PAYEMENT_ORDER_STATUS.SUCCESS);
                this.paymentOrderRepository.save(paymentOrder);
                return true;
            }

            paymentOrder.setPaymentOrderStatus(PAYEMENT_ORDER_STATUS.FAILED);
            this.paymentOrderRepository.save(paymentOrder);
            return false;

        }

        System.out.println("Payment order with " + paymentOrder.getPaymentOrderStatus().toString() +
                " status cannot be processed");

        return false;
    }


    @Override
    public PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {

        // Indian Rupee to KES (1:1.53~2(since amount is Long))

        amount = amount * 2;

        try {

            RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

            JSONObject paymentLinkRequest = getJsonObject(user, amount, orderId);

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentLinkUrl = paymentLink.get("short_url");
            String paymentLinkId = paymentLink.get("id");

            return paymentLink;

        } catch (Exception e) {

            System.out.println("Razorpay Payment Link Creation Error ..."+e.getMessage());

            throw new RazorpayException(e.getMessage());

        }
    }

    private @NotNull JSONObject getJsonObject(User user, Long amount, Long orderId) {
        JSONObject paymentLinkRequest = new JSONObject();

        paymentLinkRequest.put("amount", amount);
        paymentLinkRequest.put("currency", "KES");

        JSONObject customer = new JSONObject();
        customer.put("name", user.getFirstName() + " " + user.getLastName());
        customer.put("email", user.getEmail());
        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);
        // notify.put("sms", true);
        paymentLinkRequest.put("notify", notify);

        paymentLinkRequest.put("callback_url", frontendUrl+"payment-success/"+ orderId);
        paymentLinkRequest.put("callback_method", "get");
        return paymentLinkRequest;
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl+"payment-success/"+ orderId)
                .setCancelUrl(frontendUrl+"payment-cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("KES")
                                .setUnitAmount(amount*2)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("eShop Payment")
                                                .build()
                                ).build()
                        ).build()
                ).build();

        // Create url for payment
        Session session = Session.create(params);

        return session.getUrl();
    }
}
