package com.pfl.takeoutfood.model.dto;

import com.pfl.takeoutfood.model.domain.Setmeal;
import com.pfl.takeoutfood.model.domain.SetmealDish;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes = new ArrayList<>();
    private String categoryName;
}
