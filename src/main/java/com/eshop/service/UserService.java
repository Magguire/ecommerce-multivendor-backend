package com.eshop.service;

import com.eshop.model.User;

public interface UserService {

    User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;

}
