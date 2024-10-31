package com.eshop.controller;
import com.eshop.domain.USER_ROLE;
import com.eshop.model.VerificationCode;
import com.eshop.repository.UserRepository;
import com.eshop.request.LoginOtpRequest;
import com.eshop.request.LoginRequest;
import com.eshop.response.ApiResponse;
import com.eshop.response.AuthResponse;
import com.eshop.response.SignupRequest;
import com.eshop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor // so that we don't need to create constructors here for repositories.
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest signupRequest) throws Exception {

        String jwt = this.authService.createUser(signupRequest);

        AuthResponse authResponse = new AuthResponse();

        authResponse.setJwtToken(jwt);
        authResponse.setMessage("Registered successfully");
        authResponse.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/send/login-signup-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody LoginOtpRequest loginOtpRequest) throws Exception {

        // Add prefix signin_ in request before email to differentiate between login and sign in

        this.authService.sendLoginOtp(loginOtpRequest.getEmail(), loginOtpRequest.getRole());

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setMessage("Otp sent successfully");

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest loginRequest) {

        AuthResponse authResponse = this.authService.signIn(loginRequest);

        return ResponseEntity.ok(authResponse);
    }
}

