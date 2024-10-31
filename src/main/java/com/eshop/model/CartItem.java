package com.eshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne // Multiple cart items can belong to one cart
    @JsonIgnore // ignore cart in FrontEnd
    private Cart cart;

    @ManyToOne // Many cart items can belong to one product
    private Product product;

    private String size; // size of product

    private int quantity;

    private Integer mrpPrice;

    private Integer sellingPrice;

    private Long userId;


}

/* NB:

- Differences between int and Integer
1. int is a primitive data type while Integer is a wrapper class for the primitive data type int.
2. Default value for int is 0 while default value for Integer is null.
3. int occupies 4 bytes of memory while Integer takes more memory due to overhead object.
4. int has no methods while Integer has methods that cab be used to convert and compare values among other methods e.g
    Integer.parseInt(String) to convert String to Integer
 ----- Autoboxing and Unboxing feature automatically converts between int and Integer data types

 */