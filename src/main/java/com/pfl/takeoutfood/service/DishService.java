package com.pfl.takeoutfood.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pfl.takeoutfood.model.domain.Dish;
import com.pfl.takeoutfood.model.dto.DishDto;

import java.util.List;

/**
* @author yagam1
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2022-04-26 18:26:43
*/
public interface DishService extends IService<Dish> {

    /**
     * 添加菜品， 将口味列表添加入菜品口味表中
     * @param dishDto
     * @return
     */
    Boolean addDish(DishDto dishDto);

    /**
     * 分页查询菜品信息，还要额外获取菜品分类
     * @param page 第几页
     * @param pageSize 每页的大小
     * @param name 搜素关键词
     * @return
     */
    Page getDishList(Integer page, Integer pageSize, String name);


    /**
     * 停售 / 起售菜品
     * @param status 0 - 停售 / 1 - 起售
     * @param ids 目标菜品
     * @return
     */
    Boolean enableOrDisableDish(Integer status, String ids);

    /**
     * 根据 id 单独 / 批量 删除菜品
     *
     * @param ids
     * @return
     */
    Boolean delDishById(String ids);

    /**
     * 修改菜品信息，同时更改菜品口味表
     */
    Boolean editDish(DishDto dishDto);

    /**
     * 根据 id 获取 dishDto
     */
    DishDto getDishDtoById(String id);


    /**
     * 根据分类信息获取菜品信息
     * @param categoryId
     * @return
     */
    List<Dish> getDishListByCategory(long categoryId);
}
