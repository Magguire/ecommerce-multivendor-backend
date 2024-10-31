package com.eshop.controller;

import com.eshop.config.JwtProvider;
import com.eshop.domain.ACCOUNT_STATUS;
import com.eshop.exceptions.ProductException;
import com.eshop.exceptions.SellerException;
import com.eshop.model.Seller;
import com.eshop.model.SellerReport;
import com.eshop.model.VerificationCode;
import com.eshop.repository.VerificationCodeRepository;
import com.eshop.request.LoginRequest;
import com.eshop.response.AuthResponse;
import com.eshop.service.AuthService;
import com.eshop.service.EmailService;
import com.eshop.service.SellerReportService;
import com.eshop.service.SellerService;
import com.eshop.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    @Value("${config.param.frontend-url}")
    private String frontendUrl;

    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final SellerReportService sellerReportService;
    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest loginRequest) throws Exception {

        String email = loginRequest.getEmail();

        // Set seller prefix in email

        loginRequest.setEmail("seller_"+email);

        System.out.println("Seller Email =================="+loginRequest.getEmail());

        AuthResponse authResponse = this.authService.signIn(loginRequest);

        System.out.println("Auth Response for Login Request"+ authResponse);

        return ResponseEntity.ok(authResponse);
    }


    @PatchMapping("/verify/{email}/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String email,
                                                    @PathVariable String otp) throws Exception {

        // We use patch mapping because we want to update verify email field in seller table.

        // Added email in path variable since different emails may have the same OTP

        // VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

        VerificationCode verificationCode = this.verificationCodeRepository.findByEmail(email);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {

            throw new Exception("Cannot validate email");

        }

        Seller seller = this.sellerService.verifySeller(email, otp);

        return new ResponseEntity<Seller>(seller, HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {

        Seller savedSeller = this.sellerService.createSeller(seller);

        // Create otp and save verification code details

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());

        this.verificationCodeRepository.save(verificationCode);

        // Send otp via email to seller

        String subject = "eShop Email Verification";
        String text = "Welcome to eShop, verify your account using this link ";
        String frontendEmailUrl = frontendUrl + "verify-email";

        this.emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(),
                subject, text+frontendEmailUrl);

        return new ResponseEntity<Seller>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {

        Seller seller = this.sellerService.getSellerById(id);

        return new ResponseEntity<Seller>(seller, HttpStatus.OK);
    }


    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt)
            throws Exception {

        Seller seller = this.sellerService.getSellerProfile(jwt);

        return new ResponseEntity<Seller>(seller, HttpStatus.OK);

    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = this.sellerService.getSellerProfile(jwt);

        SellerReport sellerReport = this.sellerReportService.getSellerReport(seller);

        return new ResponseEntity<>(sellerReport, HttpStatus.OK);

    }


    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) ACCOUNT_STATUS status) {

        List<Seller> sellers = this.sellerService.getAllSellers(status);

        return ResponseEntity.ok(sellers);
    }

    @PatchMapping("/update")
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,
                                               @RequestBody Seller seller) throws Exception {

        Seller profile = this.sellerService.getSellerProfile(jwt);
        Seller updatedSeller = this.sellerService.updateSeller(profile.getId(), seller);

        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {

        this.sellerService.deleteSeller(id);

        return ResponseEntity.noContent().build(); // Return response with status code 204 and no body

    }




}
