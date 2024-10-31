package com.eshop.controller;

import com.eshop.model.Deal;
import com.eshop.response.ApiResponse;
import com.eshop.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {

    private final DealService dealService;


    @PostMapping
    public ResponseEntity<Deal> createDealHandler(@RequestBody Deal deal) {

        Deal createdDeal = this.dealService.createDeal(deal);

        return new ResponseEntity<Deal>(createdDeal, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Deal>> getAllDeals() {

        List<Deal> allDeals = this.dealService.getdeals();

        return ResponseEntity.ok(allDeals);

    }


    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeals(@PathVariable Long id,
                                            @RequestBody Deal deal) throws Exception {

        Deal updatedDeal = this.dealService.updateDeal(id, deal);

        return ResponseEntity.ok(updatedDeal);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDealHandler(@PathVariable Long id) throws Exception {

        this.dealService.deleteDeal(id);

        ApiResponse response = new ApiResponse();

        response.setMessage("Deal has been deleted successfully");

        return ResponseEntity.ok(response);

    }
}
