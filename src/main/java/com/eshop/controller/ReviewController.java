package com.eshop.controller;

import com.eshop.model.Product;
import com.eshop.model.Review;
import com.eshop.model.User;
import com.eshop.request.CreateReviewRequest;
import com.eshop.response.ApiResponse;
import com.eshop.service.ProductService;
import com.eshop.service.ReviewService;
import com.eshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {

        List<Review> reviews = this.reviewService.getReviewByProductId(productId);

        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Review> createReview(@RequestHeader("Authorization") String jwt,
                                               @PathVariable Long productId,
                                               @RequestBody CreateReviewRequest request) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);
        Product product = this.productService.findProductById(productId);
        Review review = this.reviewService.createReview(request, user, product);

        return ResponseEntity.ok(review);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@RequestHeader("Authorization") String jwt,
                                               @PathVariable Long reviewId,
                                               @RequestBody CreateReviewRequest request) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);
        Review review = this.reviewService.updateReview(reviewId, request.getReviewText(),
                request.getReviewRating(), user.getId());

        return ResponseEntity.ok(review);

    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@RequestHeader("Authorization") String jwt,
                                                    @PathVariable Long reviewId) throws Exception {

        User user = this.userService.findUserByJwtToken(jwt);

        this.reviewService.deleteReview(reviewId, user.getId());

        ApiResponse response = new ApiResponse();

        response.setMessage("Review has been deleted successfully");

        return ResponseEntity.ok(response);
    }


}
