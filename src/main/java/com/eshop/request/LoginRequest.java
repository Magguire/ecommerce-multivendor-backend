package com.eshop.request;

import com.eshop.domain.USER_ROLE;
import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String otp;
}
