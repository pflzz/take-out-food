package com.pfl.takeoutfood.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pfl.takeoutfood.model.domain.Setmeal;
import com.pfl.takeoutfood.model.dto.SetmealDto;

import java.util.List;

/**
* @author yagam1
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2022-04-26 18:27:03
*/
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐
     */
    Boolean addSetmealDto(SetmealDto setmealDto);

    /**
     * Integer page, Integer pageSize, String name
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    Page getSetmealList(Integer page, Integer pageSize, String name);

    /**
     * 停售 / 起售套餐
     * @param status 0 - 停售 / 1 - 起售
     * @param ids 目标套餐
     * @return
     */
    Boolean enableOrDisableSetmeal(Integer status, String ids);

    /**
     * 根据 id 单独 / 批量 删除套餐
     *
     * @param ids
     * @return
     */
    Boolean delSetmealById(String ids);

    /**
     * 根据 id 获取 setmealDto
     */
    SetmealDto getSetmealDtoById(String id);

    /**
     * 修改菜品信息，同时更改套餐菜品表
     */
    Boolean editSetmeal(SetmealDto setmealDto);

    /**
     * 根据分类 id 查询套餐信息
     */
    List<Setmeal>  getSetmealDishById(String categoryId);
}
