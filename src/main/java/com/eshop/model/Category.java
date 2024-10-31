package com.eshop.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;  // Installed spring-boot-starter-validation dependency
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @NotNull // Add validation dependency from pom.xml for this to work
    @Column(unique = true)
    private String categoryId;

    @ManyToOne //  Many categories belong to one parent category
    private Category parentCategory;

    @NotNull
    private Integer level;
}
