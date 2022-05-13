package com.pfl.takeoutfood.controller;

import com.pfl.takeoutfood.common.BaseResponse;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.model.domain.Orders;
import com.pfl.takeoutfood.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource
    private OrdersService ordersService;

    /**
     * 提交购物车
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public BaseResponse<String> submit(@RequestBody Orders orders) {
        if (orders == null) {
            throw new BusinessException("参数错误");
        }
        ordersService.submit(orders);
        return BaseResponse.success("提交订单成功");
    }
}
