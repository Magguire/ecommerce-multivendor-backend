package com.eshop.service;

import com.eshop.model.Product;
import com.eshop.model.User;
import com.eshop.model.Wishlist;

public interface WishlistService {

    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product);

}
