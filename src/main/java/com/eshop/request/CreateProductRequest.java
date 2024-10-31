package com.eshop.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateProductRequest {

    private String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private int discountPercentage;
    private int quantity;
    private String color;
    private String imgUrl;
    private List<String> images;
    private String category;
    private String category2;
    private String category3;
    private String sizes;
}
