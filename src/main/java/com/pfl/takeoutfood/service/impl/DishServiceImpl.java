package com.pfl.takeoutfood.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.mapper.DishFlavorMapper;
import com.pfl.takeoutfood.mapper.DishMapper;
import com.pfl.takeoutfood.model.domain.Category;
import com.pfl.takeoutfood.model.domain.Dish;
import com.pfl.takeoutfood.model.domain.DishFlavor;
import com.pfl.takeoutfood.model.dto.DishDto;
import com.pfl.takeoutfood.service.CategoryService;
import com.pfl.takeoutfood.service.DishFlavorService;
import com.pfl.takeoutfood.service.DishService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yagam1
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2022-04-26 18:26:43
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService {

    @Resource
    private DishMapper dishMapper;
    @Resource
    private CategoryService categoryService;
    @Resource
    private DishFlavorMapper dishFlavorMapper;
    @Resource
    private DishFlavorService dishFlavorService;

    /**
     * 添加菜品
     *
     * @param dishDto
     * @return
     */
    @Override
    @Transactional
    public Boolean addDish(DishDto dishDto) {
        // 添加菜品信息到菜品表中
        if (dishMapper.insert(dishDto) <= 0) {
            throw new BusinessException("添加菜品失败");
        }

        Long dishId = dishDto.getId();

        // 保存菜品口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        return dishFlavorService.saveBatch(flavors);
    }

    /**
     * 分页查询菜品信息
     *
     * @param page     第几页
     * @param pageSize 每页的大小
     * @param name     搜素关键词
     * @return
     */
    @Override
    public Page getDishList(Integer page, Integer pageSize, String name) {

        Page<Dish> pagInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        // 只有当name 不为空时才执行此条件查询
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);
        queryWrapper.orderByAsc("update_time");
        Page<Dish> dishPage = dishMapper.selectPage(pagInfo, queryWrapper);
        // 对象拷贝, 剥离掉原来的数据，附加上额外的业务信息 (CategoryName)
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> list;
        list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return dishDtoPage;
    }

    /**
     * 停售 / 起售菜品
     *
     * @param status 0 - 停售 / 1 - 起售
     * @param ids    目标菜品
     * @return
     */
    @Override
    public Boolean enableOrDisableDish(Integer status, String ids) {

        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
        String[] idList = ids.split(",");
        // 单个操作
        if (idList.length == 1) {
            if (status == 1) {

                updateWrapper.set("status", 1).eq("id", ids);

                dishMapper.update(null, updateWrapper);
                if (dishMapper.update(null, updateWrapper) > 0) {
                    return true;
                }
            }
            // 停售
            if (status == 0) {
                updateWrapper.set("status", 0).eq("id", ids);
                if (dishMapper.update(null, updateWrapper) > 0) {
                    return true;
                }

            }
        }
        // 批量操作
        // 起售
        if (status == 1) {
            updateWrapper.set("status", 1).in("id", idList);
            dishMapper.update(null, updateWrapper);
            if (dishMapper.update(null, updateWrapper) > 0) {
                return true;
            }
        }
        // 停售
        if (status == 0) {
            updateWrapper.set("status", 0).in("id", idList);
            dishMapper.update(null, updateWrapper);
            if (dishMapper.update(null, updateWrapper) > 0) {
                return true;
            }
        }
        return false;

    }

    @Override
    public Boolean delDishById(String ids) {
        String[] idList = ids.split(",");
        //  单删
        if (idList.length == 1) {
            if (dishMapper.deleteById(idList[0]) > 0) {
                return true;
            }
        }

        // 批量删除
        if (dishMapper.deleteBatchIds(Arrays.asList(idList)) > 0) {
            return true;
        }

        return false;
    }

    /**
     * 修改菜品信息，同时更改菜品口味表
     */
    @Transactional
    @Override
    public Boolean editDish(DishDto dishDto) {

        // 更改菜品信息
        if (dishMapper.updateById(dishDto) <= 0) {
            throw new BusinessException("该菜品不存在");
        }

        // 更改菜品口味信息
        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dish_id", dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

        return true;
    }

    @Override
    public DishDto getDishDtoById(String id) {
        Dish dish = getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        Long categoryId = dish.getCategoryId();
        Category category = categoryService.getById(categoryId);
        dishDto.setCategoryName(category.getName());
        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dish_id", id);
        // 是否为起售状态
        queryWrapper.eq("status", 1);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(queryWrapper);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    /**
     * 根据分类信息获取菜品信息
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getDishListByCategory(long categoryId) {
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        // 是否为起售状态
        queryWrapper.eq("status", 1);
        List<Dish> dishes = dishMapper.selectList(queryWrapper);
        return dishes;
    }


}




