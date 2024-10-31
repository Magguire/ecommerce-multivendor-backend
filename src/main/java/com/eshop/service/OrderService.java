package com.eshop.service;

import com.eshop.domain.ORDER_STATUS;
import com.eshop.model.*;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    Order  findOrderById(Long orderId) throws Exception;
    List<Order> usersOrderHistory(Long userId);
    List<Order> sellersOrder(Long sellerId);
    Order updateOrderStatus(Long orderId, ORDER_STATUS status) throws Exception;
    Order cancelOrder(Long orderId, User user) throws Exception;
    OrderItem findOrderItemById(Long orderItemId) throws Exception;
//    Set<Order> getAllOrders();

}
