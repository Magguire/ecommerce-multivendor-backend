package com.eshop.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable  // Class can be made part of another entity
@AllArgsConstructor
@NoArgsConstructor
public class BankDetails {

    private String accountHolderName;
    private String accountNumber;
    private String bankName;
    private String ifscCode;
}
