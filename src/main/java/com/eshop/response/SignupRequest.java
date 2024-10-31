package com.eshop.response;

import lombok.Data;
import lombok.Getter;

@Data  // Provides getter and setter method for this class
public class SignupRequest {

    private String email;
    private String firstName;
    private String lastName;
    private String otp;
}
