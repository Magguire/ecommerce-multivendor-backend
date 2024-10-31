package com.eshop.service.impl;

import com.eshop.domain.USER_ROLE;
import com.eshop.model.Seller;
import com.eshop.model.User;
import com.eshop.repository.SellerRepository;
import com.eshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private static final String SELLER_PREFIX = "seller_"; // Differentiates between Seller table and user table

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username.startsWith(SELLER_PREFIX)) {

            String actualUsername = username.substring(SELLER_PREFIX.length());
            Seller seller = this.sellerRepository.findByEmail(actualUsername);

            if (seller!=null){

                return this.buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
            }

        } else {
            User user = this.userRepository.findByEmail(username);

            if (user!=null) {
                return this.buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
            }
        }
        throw new UsernameNotFoundException("User or seller not found with email - "+username);
    }

    public UserDetails buildUserDetails(String email, String password, USER_ROLE role) {

        if (role==null) role = USER_ROLE.ROLE_CUSTOMER;

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(role.toString()));

        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                authorities
        );

    }
}
