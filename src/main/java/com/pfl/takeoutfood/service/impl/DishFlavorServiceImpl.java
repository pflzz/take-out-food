package com.pfl.takeoutfood.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pfl.takeoutfood.mapper.DishFlavorMapper;
import com.pfl.takeoutfood.model.domain.DishFlavor;
import com.pfl.takeoutfood.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
* @author yagam1
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2022-04-26 18:26:50
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService {

}




