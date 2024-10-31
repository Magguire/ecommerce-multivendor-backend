package com.eshop.service.impl;

import com.eshop.config.JwtProvider;
import com.eshop.domain.ACCOUNT_STATUS;
import com.eshop.domain.USER_ROLE;
import com.eshop.exceptions.SellerException;
import com.eshop.model.Address;
import com.eshop.model.Seller;
import com.eshop.repository.AddressRepository;
import com.eshop.repository.SellerRepository;
import com.eshop.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    @Override
    public Seller getSellerProfile(String jwt) throws Exception {

        String email = this.jwtProvider.getEmailFromJwtToken(jwt);

        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {

        // Find if seller with similar address exists
        Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());

        if (sellerExist!=null){

            throw new Exception("Seller with this email already exists.");

        }

        // Save seller's pickup address in Address Table
        Address savedAddress = addressRepository.save(seller.getPickUpAddress());

        // Create new seller
        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(this.passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickUpAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        this.sellerRepository.save(newSeller);

        return newSeller;
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {

        return this.sellerRepository.findById(id)
                .orElseThrow(() -> new SellerException("Seller not found with id - " + id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {

        Seller seller = this.sellerRepository.findByEmail(email);

        if (seller==null){

            throw new Exception("Seller does not exist with email - "+ email);
        }

        return seller;
    }

    @Override
    public List<Seller> getAllSellers(ACCOUNT_STATUS status) {

        if (status == null) {

            return this.sellerRepository.findAll();

        } else {

            return this.sellerRepository.findByAccountStatus(status);
        }
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {

        // Check if seller exists

        Seller sellerExists = this.getSellerById(id);

        // Check if update seller request has the fields and update

        if (seller.getSellerName() != null ) {

            sellerExists.setSellerName(seller.getSellerName());
        }

        if (seller.getMobile() != null) {

            sellerExists.setMobile(seller.getMobile());
        }

        if (seller.getEmail() != null) {

            sellerExists.setEmail(seller.getEmail());
        }

        if (seller.getBusinessDetails() != null) {

               if (seller.getBusinessDetails().getBusinessName() != null) {

                   sellerExists.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
               }

               if (seller.getBusinessDetails().getBusinessAddress() != null) {

                   sellerExists.getBusinessDetails().setBusinessAddress(seller.getBusinessDetails().getBusinessAddress());
               }

               if (seller.getBusinessDetails().getBusinessEmail() != null) {

                   sellerExists.getBusinessDetails().setBusinessEmail(seller.getBusinessDetails().getBusinessEmail());

               }

               if (seller.getBusinessDetails().getBusinessMobile() != null) {

                   sellerExists.getBusinessDetails().setBusinessMobile(seller.getBusinessDetails().getBusinessMobile());

               }

               if (seller.getBusinessDetails().getLogo() != null) {

                   sellerExists.getBusinessDetails().setLogo(seller.getBusinessDetails().getLogo());

               }

               if (seller.getBusinessDetails().getBanner() != null) {

                   sellerExists.getBusinessDetails().setBanner(seller.getBusinessDetails().getBanner());
               }

        }

        if (seller.getGSTIN() != null) {

            sellerExists.setGSTIN(seller.getGSTIN());

        }

        if (seller.getPickUpAddress() != null) {

            // New address is automatically update to Address Table due to cascade rule

            if (seller.getPickUpAddress().getAddress() != null) {

                sellerExists.getPickUpAddress().setAddress(seller.getPickUpAddress().getAddress());

            }
            if (seller.getPickUpAddress().getMobile() != null) {

                sellerExists.getPickUpAddress().setMobile(seller.getPickUpAddress().getMobile());
            }
            if (seller.getPickUpAddress().getCity() != null) {

                sellerExists.getPickUpAddress().setCity(seller.getPickUpAddress().getCity());
            }
            if (seller.getPickUpAddress().getLocality() != null) {

                sellerExists.getPickUpAddress().setLocality(seller.getPickUpAddress().getLocality());
            }

            if (seller.getPickUpAddress().getName() != null) {

                sellerExists.getPickUpAddress().setName(seller.getPickUpAddress().getName());

            }
            if (seller.getPickUpAddress().getState() != null) {

                sellerExists.getPickUpAddress().setState(seller.getPickUpAddress().getState());

            }
            if (seller.getPickUpAddress().getPinCode() != null) {

                sellerExists.getPickUpAddress().setPinCode(seller.getPickUpAddress().getPinCode());
            }

        }

        if (seller.getBankDetails() != null) {
                if (seller.getBankDetails().getAccountNumber() != null) {

                    sellerExists.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());

                }

                if (seller.getBankDetails().getBankName() != null) {

                    sellerExists.getBankDetails().setBankName(seller.getBankDetails().getBankName());

                }
                if (seller.getBankDetails().getIfscCode() != null) {

                    sellerExists.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
                }

                if (seller.getBankDetails().getAccountHolderName() != null) {

                    sellerExists.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
                }
        }


        return this.sellerRepository.save(sellerExists);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {

        Seller sellerExists = this.getSellerById(id);

        this.sellerRepository.delete(sellerExists);

    }

    @Override
    public Seller verifySeller(String email, String otp) throws Exception {

        Seller seller = this.getSellerByEmail(email);

        seller.setEmailVerified(true);

        return this.sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long id, ACCOUNT_STATUS status) throws Exception {

        Seller seller = this.getSellerById(id);

        seller.setAccountStatus(status);

        return this.sellerRepository.save(seller);
    }
}
