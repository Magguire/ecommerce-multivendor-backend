package com.eshop.model;

import com.eshop.domain.ACCOUNT_STATUS;
import com.eshop.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sellerName;

    private String mobile;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Embedded // Columns of the business details will be embedded(made part of) the Seller Entity
    private BusinessDetails businessDetails = new BusinessDetails();

    @Embedded  // Columns of the bank details will be embedded(made part of) the Seller Entity
    private BankDetails bankDetails = new BankDetails();

    @OneToOne(cascade = CascadeType.ALL) // One seller can only have one pickup address and one pickup address can only be for one seller
    private Address pickUpAddress = new Address();

    private String GSTIN;

    private USER_ROLE role = USER_ROLE.ROLE_SELLER;

    private boolean isEmailVerified = false;

    private ACCOUNT_STATUS accountStatus = ACCOUNT_STATUS.PENDING_VERIFICATION;


}
