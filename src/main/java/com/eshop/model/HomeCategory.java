package com.eshop.model;

import com.eshop.domain.HOME_CATEGORY_SECTION;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HomeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Generate Id automatically
    private Long id;

    private String name;

    private String image;

    private String categoryId;

    private HOME_CATEGORY_SECTION section;
}
