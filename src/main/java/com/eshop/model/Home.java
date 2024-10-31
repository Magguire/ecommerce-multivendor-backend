package com.eshop.model;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Home {

    private List<HomeCategory> grid;

    private List<HomeCategory> shopByCategories;

    private List<HomeCategory> electronicCategories;

    private List<HomeCategory> dealCategories;

    private List<Deal> deals;


}
