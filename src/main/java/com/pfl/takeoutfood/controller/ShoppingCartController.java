package com.pfl.takeoutfood.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pfl.takeoutfood.common.BaseContext;
import com.pfl.takeoutfood.common.BaseResponse;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.model.domain.ShoppingCart;
import com.pfl.takeoutfood.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 往购物车添加菜品 / 套餐
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        // 1. 设置用户id，指定当前是哪个用户在添加购物车
        Long userId = BaseContext.getId();

        // 2. 查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (dishId != null) {
            // 添加菜品
            queryWrapper.eq("dish_id", dishId);
        } else {
            // 添加套餐
            queryWrapper.eq("setmeal_id", shoppingCart.getSetmealId());
        }

        // 3. 如果已存在，在原有的数量 + 1
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne != null) {
            cartServiceOne.setUserId(userId);
            cartServiceOne.setCreateTime(LocalDateTime.now());
            cartServiceOne.setNumber(cartServiceOne.getNumber() + 1);
            // 执行的是更新操作！！！
            shoppingCartService.updateById(cartServiceOne);
        } else {
            // 4. 如果不存在，将 number 设置为1
            cartServiceOne = shoppingCart;
            cartServiceOne.setUserId(userId);
            cartServiceOne.setCreateTime(LocalDateTime.now());
            cartServiceOne.setNumber(1);
            // // 执行的是插入操作！！！
            shoppingCartService.save(cartServiceOne);
        }
        return BaseResponse.success(cartServiceOne);
    }

    /**
     * 返回购物车列表信息
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<ShoppingCart>> list() {
        Long userId = BaseContext.getId();
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return BaseResponse.success(list);
    }

    /**
     * 购物车菜品 / 套餐数量 - 1
     * @return
     */
    @PostMapping("/sub")
    public BaseResponse<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {

       // 1. 设置用户id，指定当前是哪个用户在添加购物车
        Long userId = BaseContext.getId();

        // 2. 查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (dishId != null) {
            // 菜品数量 - 1
            queryWrapper.eq("dish_id", dishId);
        } else {
            // 套餐数量 - 1
            queryWrapper.eq("setmeal_id", shoppingCart.getSetmealId());
        }

        // 3. 如果已存在，在原有的数量 - 1
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne != null && cartServiceOne.getNumber() > 0) {
            cartServiceOne.setUserId(userId);
            cartServiceOne.setCreateTime(LocalDateTime.now());
            cartServiceOne.setNumber(cartServiceOne.getNumber() - 1);
            // 执行的是更新操作！！！
            shoppingCartService.updateById(cartServiceOne);
        } else {
            shoppingCartService.removeById(cartServiceOne);
            throw new BusinessException("购物车中无此类商品");
        }
        return BaseResponse.success(cartServiceOne);
    }


    /**
     * 清空购物车
     */
    @DeleteMapping("/clean")
    public BaseResponse<String> clean() {
        Long userId = BaseContext.getId();
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        shoppingCartService.remove(queryWrapper);
        return BaseResponse.success("清空成功");
    }
}
