package com.pfl.takeoutfood.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pfl.takeoutfood.mapper.ShoppingCartMapper;
import com.pfl.takeoutfood.model.domain.ShoppingCart;
import com.pfl.takeoutfood.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
* @author yagam1
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2022-04-26 18:27:09
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService {

}




