package com.eshop.request;


import lombok.Data;

@Data
public class addCartItemRequest {

    private String size;
    private int quantity;
    private Long productId;
}
