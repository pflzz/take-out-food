package com.pfl.takeoutfood.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pfl.takeoutfood.model.domain.Orders;

/**
* @author yagam1
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2022-04-26 18:27:00
*/
public interface OrdersService extends IService<Orders> {
    /**
     * 提交购物车
     * @param orders
     * @return
     */
    void submit(Orders orders);
}
