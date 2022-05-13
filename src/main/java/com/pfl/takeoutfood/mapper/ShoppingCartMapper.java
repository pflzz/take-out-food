package com.pfl.takeoutfood.mapper;

import com.pfl.takeoutfood.model.domain.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yagam1
* @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
* @createDate 2022-04-26 18:27:09
* @Entity generator.domain.ShoppingCart
*/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}




