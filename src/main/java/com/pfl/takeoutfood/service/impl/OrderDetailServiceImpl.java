package com.pfl.takeoutfood.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pfl.takeoutfood.mapper.OrderDetailMapper;
import com.pfl.takeoutfood.model.domain.OrderDetail;
import com.pfl.takeoutfood.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
* @author yagam1
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-04-26 18:26:57
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService {

}




