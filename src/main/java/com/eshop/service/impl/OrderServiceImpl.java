package com.eshop.service.impl;

import com.eshop.domain.ORDER_STATUS;
import com.eshop.domain.PAYMENT_STATUS;
import com.eshop.model.*;
import com.eshop.repository.AddressRepository;
import com.eshop.repository.OrderItemRepository;
import com.eshop.repository.OrderRepository;
import com.eshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AddressRepository addressRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {

        // Add shipping address to user's set of addresses
        user.getAddresses().add(shippingAddress);

        Address address = this.addressRepository.save(shippingAddress);

        /* Create different orders for each seller - Key(sellerId) : Value(cartItem)
        * stream() - Converts the list of CartItem objects into a Stream,
        * allowing operations like filtering, mapping, and grouping to be performed in a functional style.
        * collect(): collects the elements of the stream into a Map.
        * Collectors.groupingBy(...): captures the grouping criterion specification.
        * item -> item.getProduct().getSeller().getId(): lambda function that specifies the grouping criterion.
        *
        * */

        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().
                stream().collect(Collectors.groupingBy(item ->item.getProduct().getSeller().getId()));

        // Create a set of orders
        Set<Order> orders = new HashSet<>();

        // Create loop
        for (Map.Entry<Long, List<CartItem>> entry: itemsBySeller.entrySet()) {

            Long sellerId = entry.getKey();
            List<CartItem> cartItems = entry.getValue();

            int totalOrderPrice = cartItems.stream().mapToInt(CartItem::getSellingPrice).sum();

            int totalItem = cartItems.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder = new Order();

            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setTotalItems(totalItem);
            createdOrder.setShippingAddress(shippingAddress);
            createdOrder.setOrderStatus(ORDER_STATUS.PENDING);
            createdOrder.getPaymentDetails().setStatus(PAYMENT_STATUS.PENDING);

            Order savedOrder = this.orderRepository.save(createdOrder);

            orders.add(savedOrder);

            List<OrderItem> orderItems = new ArrayList<>();

            for (CartItem item: cartItems){

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setMrpPrice(item.getMrpPrice());
                    orderItem.setProduct(item.getProduct());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setSize(item.getSize());
                    orderItem.setUserId(item.getUserId());
                    orderItem.setSellingPrice(item.getSellingPrice());

                    savedOrder.getOrderItems().add(orderItem);

                    OrderItem savedOrderItem = this.orderItemRepository.save(orderItem);
                    orderItems.add(savedOrderItem);


            }
        }

        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {

        return this.orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order does not exist with id - " + orderId));
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {

        return this.orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {

        return this.orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, ORDER_STATUS status) throws Exception {

        Order orderExists = this.findOrderById(orderId);

        orderExists.setOrderStatus(status);

        return this.orderRepository.save(orderExists);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {

        Order orderExists = this.findOrderById(orderId);

        if (!user.getId().equals(orderExists.getUser().getId())){

            orderExists.setOrderStatus(ORDER_STATUS.CANCELLED);

            return this.orderRepository.save(orderExists);

        } else {

            throw new Exception("You cannot cancel this order.");

        }


    }

    @Override
    public OrderItem findOrderItemById(Long orderItemId) throws Exception {

        return this.orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new Exception("Order item does not exist with id - " + orderItemId));
    }
}
