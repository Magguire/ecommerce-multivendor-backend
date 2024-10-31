package com.eshop.controller;

import com.eshop.model.Home;
import com.eshop.model.HomeCategory;
import com.eshop.service.HomeCategoryService;
import com.eshop.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home/categories")
public class HomeCategoryController {

    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;


    @PostMapping
    public ResponseEntity<Home> createHomeCategories(@RequestBody List<HomeCategory> homeCategories)
            throws Exception {

        List<HomeCategory> newHomeCategories = this.homeCategoryService.createHomeCategories(homeCategories);

        Home home = this.homeService.createHomePageData(newHomeCategories);

        return new ResponseEntity<Home>(home, HttpStatus.CREATED);

    }

    @GetMapping("/admin/home-categories")
    public ResponseEntity<List<HomeCategory>> getAllCategories() {

        List<HomeCategory> homeCategories = this.homeCategoryService.getAllHomeCategories();

        return new ResponseEntity<List<HomeCategory>>(homeCategories, HttpStatus.OK);

    }

    @PatchMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(@PathVariable Long id,
                                                           @RequestBody HomeCategory homeCategory)
            throws Exception {

        HomeCategory updatedHomeCategory = this.homeCategoryService.updateHomeCategory(homeCategory, id);

        return ResponseEntity.ok(updatedHomeCategory);
    }

}
