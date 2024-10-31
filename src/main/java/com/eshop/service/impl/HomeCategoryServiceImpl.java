package com.eshop.service.impl;

import com.eshop.model.HomeCategory;
import com.eshop.repository.HomeCategoryRepository;
import com.eshop.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    @PreAuthorize("hasRole ('ADMIN')") // Check if user has admin role
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {

        return this.homeCategoryRepository.save(homeCategory);
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')") // Check if user has admin role
    public List<HomeCategory> createHomeCategories(List<HomeCategory> homeCategories) throws Exception {

            if(this.getAllHomeCategories().isEmpty()) {

                return this.homeCategoryRepository.saveAll(homeCategories);

            } else throw new Exception("Home category list is not empty");
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')") // Check if user has admin role
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception {

        HomeCategory existingHomeCategory = this.findHomeCategoryById(id);

        if (homeCategory.getImage() != null) {
            existingHomeCategory.setImage(homeCategory.getImage());

        }

        if (homeCategory.getCategoryId() != null) {

            existingHomeCategory.setCategoryId(homeCategory.getCategoryId());
        }


        return this.homeCategoryRepository.save(existingHomeCategory);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {

        return this.homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory findHomeCategoryById(Long id) throws Exception {
        return this.homeCategoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Home category does not exist with id - "+id));
    }
}
