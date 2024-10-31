package com.eshop.service;

import com.eshop.exceptions.ProductException;
import com.eshop.model.Product;
import com.eshop.model.Seller;
import com.eshop.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Product createProduct(CreateProductRequest request, Seller seller);
    void deleteProduct(Long productId) throws ProductException;
    Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> searchProduct(String query);
    Page<Product> getAllProducts(
            String category,
            String brand,
            String color,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String stock,
            String sort,
            Integer pageNumber

    );

    List<Product> getProductBySellerId(Long sellerId);

}
