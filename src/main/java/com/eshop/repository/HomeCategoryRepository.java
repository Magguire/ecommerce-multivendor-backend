package com.eshop.repository;

import com.eshop.model.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {
}
