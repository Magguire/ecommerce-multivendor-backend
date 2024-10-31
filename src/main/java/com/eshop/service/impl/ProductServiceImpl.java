package com.eshop.service.impl;

import com.eshop.exceptions.ProductException;
import com.eshop.model.Category;
import com.eshop.model.Product;
import com.eshop.model.Seller;
import com.eshop.repository.CategoryRepository;
import com.eshop.repository.ProductRepository;
import com.eshop.request.CreateProductRequest;
import com.eshop.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest request, Seller seller) {


        // Save product category to Category Table

        Category category1 = this.categoryRepository.findByCategoryId(request.getCategory());

        if (category1 == null){
            Category category = new Category();
            category.setCategoryId(request.getCategory());
            category.setLevel(1);

            category1 = this.categoryRepository.save(category);

        }

        Category category2 = this.categoryRepository.findByCategoryId(request.getCategory2());

        if (category2 == null){
            Category category = new Category();
            category.setCategoryId(request.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);

            category2 = this.categoryRepository.save(category);

        }

        Category category3 = this.categoryRepository.findByCategoryId(request.getCategory3());

        if (category3 == null){
            Category category = new Category();
            category.setCategoryId(request.getCategory3());
            category.setLevel(3);
            category.setParentCategory(category2);

            category3 = this.categoryRepository.save(category);

        }

        // Calculate discount percentage as (sellingPrice - mrpPrice) / mrpPrice * 100

        int mrpPrice = request.getMrpPrice();
        int sellingPrice = request.getSellingPrice();

        int discountPercentage = calculateDiscountPercentage(mrpPrice, sellingPrice);

        // Save product to Product Table

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(request.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setColor(request.getColor());
        product.setTitle(request.getTitle());
        product.setSellingPrice(sellingPrice);
        product.setMrpPrice(mrpPrice);
        product.setQuantity(request.getQuantity());
        product.setImages(request.getImages());
        product.setSizes(product.getSizes());
        product.setDiscountPercentage(discountPercentage);

        return productRepository.save(product);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {

        if (mrpPrice <= 0) {

            throw new IllegalArgumentException("Actual price must be greater than 0");
        }

        // Cast numerator to double

        double discountPercentage = (double)(mrpPrice - sellingPrice) / mrpPrice * 100;

        return  (int)discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {

        Product product = this.findProductById(productId);

        this.productRepository.delete(product);

    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {

        Product productExists = this.findProductById(productId);

        product.setId(productId);

        int mrpPrice = productExists.getMrpPrice();

        int sellingPrice = productExists.getSellingPrice();

        if (product.getColor() != null){

            productExists.setColor(product.getColor());
        }

        if (product.getImages() != null){

            productExists.setImages(product.getImages());
        }

        if (product.getDescription() != null) {

            productExists.setDescription(product.getDescription());
        }

        if (product.getSizes() != null) {

            productExists.setSizes(product.getSizes());
        }

        if (product.getMrpPrice() > 0) {

            mrpPrice = product.getMrpPrice();

            productExists.setMrpPrice(product.getMrpPrice());

        }

        if (product.getSellingPrice() > 0) {

            sellingPrice = product.getSellingPrice();

            productExists.setSellingPrice(product.getSellingPrice());

        }

        if (product.getMrpPrice() > 0 || product.getSellingPrice() > 0) {

            int discountPercentage = this.calculateDiscountPercentage(mrpPrice, sellingPrice);

            product.setDiscountPercentage(discountPercentage);
        }

        if (product.getCategory() != null){

            if (product.getCategory().getCategoryId() != null){

                productExists.getCategory().setCategoryId(product.getCategory().getCategoryId());
            }

            if (product.getCategory().getParentCategory() != null){

                productExists.getCategory().setParentCategory(product.getCategory().getParentCategory());
            }

            if (product.getCategory().getName() != null){

                productExists.getCategory().setName(product.getCategory().getName());
            }

            if (product.getCategory().getLevel() != null){

                productExists.getCategory().setLevel(product.getCategory().getLevel());
            }

        }


        return productRepository.save(productExists);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {

        return productRepository.findById(productId).
                orElseThrow(()-> new ProductException("Product not found with id -" + productId));

    }

    @Override
    public List<Product> searchProduct(String query) {

        return this.productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String color, String sizes,
                                        Integer minPrice, Integer maxPrice, Integer minDiscount,
                                        String stock, String sort, Integer pageNumber) {

        // Implement filtering feature

        Specification<Product> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Filter by category if exists in query

            if (category != null) {

                // Join Product and Category tables.

                Join<Product, Category> productCategoryJoin = root.join("category");

                // Filter for category in query

                predicates.add(criteriaBuilder.equal(productCategoryJoin.get("categoryId"), category));
            }

            if (color != null){

                predicates.add(criteriaBuilder.like(root.get("color"), color));
            }

            if (sizes != null && !sizes.isEmpty()) {

                predicates.add(criteriaBuilder.like(root.get("sizes"), sizes));

            }

            if (minPrice != null) {

                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }

            if (maxPrice != null) {

                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));

            }

            if (minDiscount != null) {

                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"), minDiscount));
            }

            if (stock != null) {

                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Implement sorting feature

        Pageable pageable;

        if (sort != null && !sort.isEmpty()) {

            pageable = switch (sort) {

                // If sort value price

                case "price_low" -> PageRequest.of(pageNumber != null ? pageNumber : 0,
                        10, Sort.by("sellingPrice").ascending());
                case "price_high" -> PageRequest.of(pageNumber != null ? pageNumber : 0,
                        10, Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0,
                        10, Sort.unsorted());
            };
        } else {

            pageable = PageRequest.of(pageNumber != null? pageNumber: 0, 10, Sort.unsorted());

        }

        return this.productRepository.findAll(specification, pageable); // This find all method uses JPA specification executor extension
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {

        return productRepository.findBySellerId(sellerId);
    }
}
