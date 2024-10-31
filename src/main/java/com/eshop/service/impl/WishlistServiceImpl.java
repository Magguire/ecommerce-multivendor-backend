package com.eshop.service.impl;

import com.eshop.model.Product;
import com.eshop.model.User;
import com.eshop.model.Wishlist;
import com.eshop.repository.WishlistRepository;
import com.eshop.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public Wishlist createWishlist(User user) {

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        return this.wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist getWishlistByUserId(User user) {

        Wishlist wishlist = this.wishlistRepository.findByUserId(user.getId());

        if (wishlist == null) {

            wishlist = this.createWishlist(user);
        }

        return wishlist;
    }

    @Override
    public Wishlist addProductToWishlist(User user, Product product) {

        Wishlist wishlist = this.getWishlistByUserId(user);

        // Remove product from wishlist it exists

        if (wishlist.getProducts().contains(product)) {

            wishlist.getProducts().remove(product);

        } else wishlist.getProducts().add(product);

        return wishlistRepository.save(wishlist);
    }
}