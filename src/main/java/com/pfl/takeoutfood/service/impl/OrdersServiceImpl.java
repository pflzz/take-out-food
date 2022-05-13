package com.pfl.takeoutfood.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pfl.takeoutfood.common.BaseContext;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.mapper.OrdersMapper;
import com.pfl.takeoutfood.model.domain.*;
import com.pfl.takeoutfood.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
* @author yagam1
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2022-04-26 18:27:00
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private OrderDetailService orderDetailService;
    @Resource
    private AddressBookService addressBookService;
    @Resource
    private UserService userService;

    /**
     * 提交购物车
     * @param orders
     * @return
     */
    @Override
    @Transactional
    public void submit(Orders orders) {
        // 1. 获取用户id
        Long userId = BaseContext.getId();
        User user = userService.getById(userId);
        // 获取用户的地址信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new BusinessException("用户地址信息有误，不能下单");
        }
        // 2. 向订单表插入数据
        long orderId = IdWorker.getId();
        orders.setOrderTime(LocalDateTime.now());
        orders.setNumber(String.valueOf(orderId));
        orders.setAddress(addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName() +
                addressBook.getCityName() == null ? "" : addressBook.getCityName() +
                addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName() +
                addressBook.getDetail() == null ? "" : addressBook.getDetail());
        orders.setUserId(userId);
        orders.setStatus(2); // 待派送
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());




        // 在计算总金额的同时，获取订单明细表的信息
        AtomicInteger amount = new AtomicInteger(0); // 多线程安全
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        if (shoppingCarts == null) {
            throw new BusinessException("购物车为空");
        }
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setOrderId(orderId);
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setCheckoutTime(LocalDateTime.now());
        ordersMapper.insert(orders);
        // 3. 向订单明细表插入数据
        orderDetailService.saveBatch(orderDetails);
        // 4. 删除购物车中的数据
        shoppingCartService.remove(queryWrapper);
    }
}




