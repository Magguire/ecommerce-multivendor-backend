package com.eshop.controller;

import com.eshop.exceptions.ProductException;
import com.eshop.model.Category;
import com.eshop.model.Product;
import com.eshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws ProductException {

        Product product = this.productService.findProductById(id);

        return ResponseEntity.ok(product);
    }

    @GetMapping("/search") // e.g /search?query=shirt
    public ResponseEntity<List<Product>> searchProduct(@RequestParam(required = false) String query) {

        List<Product> products = this.productService.searchProduct(query);

        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(required = false) String category,
                                                        @RequestParam(required = false) String brand,
                                                        @RequestParam(required = false) String color,
                                                        @RequestParam(required = false) String sizes,
                                                        @RequestParam(required = false) Integer minPrice,
                                                        @RequestParam(required = false) Integer maxPrice,
                                                        @RequestParam(required = false) Integer minDiscount,
                                                        @RequestParam(required = false) String stock,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(defaultValue = "0") Integer pageNumber) {

        Page<Product> products = this.productService.getAllProducts(category, brand, color, sizes, minPrice, maxPrice,
                minDiscount, stock, sort, pageNumber);

        return ResponseEntity.ok(products);
    }
}
