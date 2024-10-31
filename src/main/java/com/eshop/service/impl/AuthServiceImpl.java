package com.eshop.service.impl;

import com.eshop.config.JwtProvider;
import com.eshop.domain.USER_ROLE;
import com.eshop.model.Cart;
import com.eshop.model.Seller;
import com.eshop.model.User;
import com.eshop.model.VerificationCode;
import com.eshop.repository.CartRepository;
import com.eshop.repository.SellerRepository;
import com.eshop.repository.UserRepository;
import com.eshop.repository.VerificationCodeRepository;
import com.eshop.request.LoginRequest;
import com.eshop.response.AuthResponse;
import com.eshop.response.SignupRequest;
import com.eshop.service.AuthService;
import com.eshop.service.EmailService;
import com.eshop.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final UserDetailsServiceImpl userDetailsService;
    private final SellerRepository sellerRepository;


    @Override
    public void sendLoginOtp(String email, USER_ROLE role) throws Exception {

        String SIGNING_PREFIX = "signin_"; // Differentiate between otp for signin and signup

        if (email.startsWith(SIGNING_PREFIX)) {

            email = email.substring(SIGNING_PREFIX.length());

            // Check if email is for user or customer

            if (role.equals(USER_ROLE.ROLE_SELLER)) {

                Seller seller = this.sellerRepository.findByEmail(email);

                if(seller==null) {

                    throw new Exception("Seller with provided email does not exist.");
                }
            }

            else {

                User user = this.userRepository.findByEmail(email);

                if(user==null) {

                    throw new Exception("User with provided email does not exist.");
                }

            }

        }

        VerificationCode isExists = this.verificationCodeRepository.findByEmail(email);

        // Delete existing verification code if it exists
        if (isExists!=null) {

            verificationCodeRepository.delete(isExists);
        }

        // Create new OTP

        String otp = OtpUtil.generateOtp();

        // Save email and otp to verification code table

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);

        this.verificationCodeRepository.save(verificationCode);

        // Send otp to user via email

        String subject = "eShop SignUp OTP";
        String text = "Your signup otp is " + otp;
        emailService.sendVerificationOtpEmail(email, otp, subject, text);

    }

    @Override
    public String createUser(SignupRequest signupRequest) throws Exception {

        // Check if user has verification code
        VerificationCode verificationCode = this.verificationCodeRepository.findByEmail(signupRequest.getEmail());

        // Throw exception is verification code is null or does not match otp entered by user
        if (verificationCode==null || !verificationCode.getOtp().equals(signupRequest.getOtp())) {

            throw new Exception("Wrong otp...");
        }

        // Check if email exists
        User user = this.userRepository.findByEmail(signupRequest.getEmail());

        // Create new user if user does not exist
        if (user==null) {

            // Save new user to db

            User createdUser = new User();
            createdUser.setEmail(signupRequest.getEmail());
            createdUser.setFirstName(signupRequest.getFirstName());
            createdUser.setLastName(signupRequest.getLastName());
            createdUser.setMobile("254701234567");
            createdUser.setPassword(this.passwordEncoder.encode(signupRequest.getOtp()));
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);

            user = this.userRepository.save(createdUser);

            // Create cart for the new user
            Cart cart = new Cart();
            cart.setUser(user);
            this.cartRepository.save(cart);

        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                signupRequest.getEmail(), null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return this.jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signIn(LoginRequest loginRequest) {

        String username = loginRequest.getEmail();
        String otp = loginRequest.getOtp();

        Authentication authentication = this.authenticate(username, otp);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = this.jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(token);
        authResponse.setMessage("Login success");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {

        String SELLER_PREFIX = "seller_"; // Required when fetching email from verification code table.

        // Find if username exists

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        // Throw exception is user does not exist

        if (userDetails==null){

            throw new BadCredentialsException("Invalid username");
        }

        // Check if verification code with user is present

        if (username.startsWith(SELLER_PREFIX)) {

            username = username.substring(SELLER_PREFIX.length());
        }

        VerificationCode verificationCode = this.verificationCodeRepository.findByEmail(username);

        if (verificationCode==null || !verificationCode.getOtp().equals(otp)){

            throw new BadCredentialsException("Invalid otp");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
