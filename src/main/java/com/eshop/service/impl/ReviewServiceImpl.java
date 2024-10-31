package com.eshop.service.impl;

import com.eshop.model.Product;
import com.eshop.model.Review;
import com.eshop.model.User;
import com.eshop.repository.ReviewRepository;
import com.eshop.request.CreateReviewRequest;
import com.eshop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;


    @Override
    public Review createReview(CreateReviewRequest request, User user, Product product) {

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getReviewRating());
        review.setReviewText(request.getReviewText());
        review.setProductImages(request.getProductImages());

        product.getReviews().add(review);

        return this.reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {

        return this.reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String text, double rating, Long userId) throws Exception {

        Review review = this.findReviewById(reviewId);

        if (review.getUser().getId().equals(userId)){

            review.setReviewText(text);
            review.setRating(rating);

            return this.reviewRepository.save(review);

        } else {

            throw new Exception("You cannot update this review");
        }
    }

    @Override
    public Review findReviewById(Long id) throws Exception {

        return this.reviewRepository.findById(id)
                .orElseThrow(() -> new Exception("Review does not exist with id - "+id));
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {

        Review review = this.findReviewById(reviewId);

        if (review.getUser().getId().equals(userId)){

            this.reviewRepository.delete(review);

        } else {

            throw new Exception("You cannot delete this review");
        }

    }
}
