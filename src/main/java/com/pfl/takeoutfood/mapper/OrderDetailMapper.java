package com.pfl.takeoutfood.mapper;

import com.pfl.takeoutfood.model.domain.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yagam1
* @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
* @createDate 2022-04-26 18:26:57
* @Entity generator.domain.OrderDetail
*/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




