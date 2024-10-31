package com.eshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Generate Id automatically
    private Long id;

    private String otp;

    private String email;

    @OneToOne // One user has one verification code and one code belongs to one user
    private User user;

    @OneToOne // One seller has one verification code
    private Seller seller;
}


/*
Verification code belongs to either the user or the seller, therefore, if seller is not null then user is null and
vice-versa.
 */
