package com.eshop.service;

import com.eshop.domain.USER_ROLE;
import com.eshop.request.LoginRequest;
import com.eshop.response.AuthResponse;
import com.eshop.response.SignupRequest;

public interface AuthService {

    void sendLoginOtp(String email, USER_ROLE role) throws Exception;

    String createUser(SignupRequest signupRequest) throws Exception;

    AuthResponse signIn(LoginRequest loginRequest);
}
