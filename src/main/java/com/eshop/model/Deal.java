package com.eshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Generate Id automatically
    private Long id;

    private Integer discount;

    @OneToOne // One deal, one category
    private HomeCategory category;
}
