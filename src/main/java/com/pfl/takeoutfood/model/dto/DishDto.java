package com.pfl.takeoutfood.model.dto;

import com.pfl.takeoutfood.model.domain.Dish;
import com.pfl.takeoutfood.model.domain.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;
}
