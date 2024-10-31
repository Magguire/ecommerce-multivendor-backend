package com.eshop.service;

import com.eshop.model.Product;
import com.eshop.model.Review;
import com.eshop.model.User;
import com.eshop.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {

    Review createReview(CreateReviewRequest request, User user, Product product);
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long reviewId, String text, double rating, Long userId) throws Exception;
    Review findReviewById(Long id) throws Exception;
    void deleteReview(Long reviewId, Long userId) throws Exception;


}
