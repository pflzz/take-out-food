package com.pfl.takeoutfood.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pfl.takeoutfood.common.BaseResponse;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.model.domain.Dish;
import com.pfl.takeoutfood.model.domain.DishFlavor;
import com.pfl.takeoutfood.model.dto.DishDto;
import com.pfl.takeoutfood.service.DishFlavorService;
import com.pfl.takeoutfood.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Resource
    private DishService dishService;
    @Resource
    private DishFlavorService dishFlavorService;

    /**
     * 添加菜品, 将口味列表添加入菜品口味表中
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public BaseResponse<String> addDish(@RequestBody DishDto dishDto) {
        if (dishDto == null) {
            throw new BusinessException("参数错误");
        }
        if (dishService.addDish(dishDto)) {
            return BaseResponse.success("添加成功");
        }
        return BaseResponse.error("添加失败");
    }

    /**
     * 分页查询菜品信息, 还要额外过去菜品的分类
     *
     * @param page     第几页
     * @param pageSize 每页的大小
     * @param name     搜素关键词
     * @return
     */
    @GetMapping("/page")
    public BaseResponse<Page> getDishList(Integer page, Integer pageSize, String name) {
        if (page == null || pageSize == null) {
            throw new BusinessException("参数错误");
        }

        Page dishList = dishService.getDishList(page, pageSize, name);
        return BaseResponse.success(dishList);
    }

    /**
     * 根据 id 单独 / 批量 删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public BaseResponse<String> delDishById(String ids) {
        if (ids == null) {
            throw new BusinessException("参数错误");
        }

        if (dishService.delDishById(ids)) {
            return BaseResponse.success("删除成功");
        }
        throw new BusinessException("此菜品不存在，删除失败");
    }

    /**
     * 批量 / 单个 停售 / 起售菜品
     *
     * @param status 0 - 停售 / 1 - 起售
     * @param ids    目标菜品
     * @return
     */
    @PostMapping("/status/{status}")
    public BaseResponse<String> enableOrDisableDish(@PathVariable Integer status, String ids) {
        if (status == null || ids == null) {
            throw new BusinessException("参数错误");
        }
        if (dishService.enableOrDisableDish(status, ids)) {
            return BaseResponse.success("操作成功");
        }
        return BaseResponse.error("操作失败");
    }

    /**
     * 根据 id 修改菜品信息，并改变口味表信息
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public BaseResponse<String> editDish(@RequestBody DishDto dishDto) {
        if (dishDto == null) {
            throw new BusinessException("参数错误");
        }
        if (dishService.editDish(dishDto)) {

            return BaseResponse.success("修改成功");
        }
        return BaseResponse.error("修改失败");
    }

    /**
     * 根据 id 查询菜品信息
     */
    @GetMapping("/{id}")
    public BaseResponse<DishDto> getDishById(@PathVariable String id) {
        if (id == null) {
            throw new BusinessException("参数错误");
        }
        DishDto dishDto = dishService.getDishDtoById(id);
        if (dishDto == null) {
            throw new BusinessException("该菜品不存在");
        }
        return BaseResponse.success(dishDto);

    }


    /**
     * 根据分类信息获取菜品信息
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<DishDto>> getDishListByCategory(Long categoryId) {
        if (categoryId == null) {
            throw new BusinessException("参数错误");
        }
        List<Dish> list = dishService.getDishListByCategory(categoryId);

        // 为了在前台展示菜品的口味信息，返回的数据附加上额外的口味表信息
        List<DishDto> dishDtoList;
        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dish_id", item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        return BaseResponse.success(dishDtoList);
    }
}
