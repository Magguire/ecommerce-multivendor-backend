package com.eshop.controller;

import com.eshop.config.JwtProvider;
import com.eshop.exceptions.ProductException;
import com.eshop.model.Product;
import com.eshop.model.Seller;
import com.eshop.request.CreateProductRequest;
import com.eshop.service.ProductService;
import com.eshop.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers/products")
public class SellerProductController {

    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Product>> getProductBySellerId(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = this.sellerService.getSellerProfile(jwt);

        List<Product> products = this.productService.getProductBySellerId(seller.getId());

        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestHeader("Authorization") String jwt,
                                                 @RequestBody CreateProductRequest request) throws Exception {

        Seller seller = this.sellerService.getSellerProfile(jwt);

        Product product = this.productService.createProduct(request, seller);

        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws ProductException {

        try {
            this.productService.deleteProduct(productId);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (ProductException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody Product product) throws ProductException {


        Product updatedProduct = this.productService.updateProduct(productId, product);

        return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);

    }
}
