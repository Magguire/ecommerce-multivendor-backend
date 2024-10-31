package com.eshop.service.impl;

import com.eshop.config.JwtProvider;
import com.eshop.model.User;
import com.eshop.repository.UserRepository;
import com.eshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    @Override
    public User findUserByJwtToken(String jwt) throws Exception {

        String email = this.jwtProvider.getEmailFromJwtToken(jwt);
        return this.findUserByEmail(email);

    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        User user = this.userRepository.findByEmail(email);

        if (user==null){

            throw new Exception("User not found with email - " + email);
        }

        return user;
    }
}