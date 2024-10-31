package com.eshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)  // @NotNull is still okay
    private String reviewText;

    @Column(nullable = false)
    private double rating;

    @ElementCollection
    private List<String> productImages;

    @JsonIgnore
    @ManyToOne // One product can have several reviews
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne // One user can have multiple reviews
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


}
