package com.pfl.takeoutfood.mapper;

import com.pfl.takeoutfood.model.domain.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yagam1
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2022-04-26 18:26:43
* @Entity generator.domain.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




