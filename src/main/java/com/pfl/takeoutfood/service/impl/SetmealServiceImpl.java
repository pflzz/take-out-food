package com.pfl.takeoutfood.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.mapper.CategoryMapper;
import com.pfl.takeoutfood.mapper.SetmealDishMapper;
import com.pfl.takeoutfood.mapper.SetmealMapper;
import com.pfl.takeoutfood.model.domain.Category;
import com.pfl.takeoutfood.model.domain.Setmeal;
import com.pfl.takeoutfood.model.domain.SetmealDish;
import com.pfl.takeoutfood.model.dto.SetmealDto;
import com.pfl.takeoutfood.service.SetmealDishService;
import com.pfl.takeoutfood.service.SetmealService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yagam1
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2022-04-26 18:27:03
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {

    @Resource
    private SetmealMapper setmealMapper;
    @Resource
    private SetmealDishMapper setmealDishMapper;
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     */
    @Override
    public Boolean addSetmealDto(SetmealDto setmealDto) {

        // 向 setmeal 表插入数据
        if (setmealMapper.insert(setmealDto) <= 0) {
            throw new BusinessException("添加套餐失败");
        }

        // 向 steal_dish 表中插入数据
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId() + "");
            if (setmealDishMapper.insert(item) <= 0) {
                throw new BusinessException("添加套餐失败");
            }
            return item;
        }).collect(Collectors.toList());

        return true;
    }

    /**
     * Integer page, Integer pageSize, String name
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page getSetmealList(Integer page, Integer pageSize, String name) {
        // 查询 正常表中的信息
        Page<Setmeal> setmealPage = new Page<>();
        Page<SetmealDto> setmealDtoPage = new Page<>();
        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);
        queryWrapper.orderByAsc("code");
        queryWrapper.orderByDesc("update_time");
        Page<Setmeal> pageInfo = setmealMapper.selectPage(setmealPage, queryWrapper);


        // 将 record 分离出来，进行业务处理，再放回响应数据中
        BeanUtils.copyProperties(pageInfo, setmealDtoPage);
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list;
        list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryMapper.selectById(item.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return setmealDtoPage;
    }
    /**
     * 停售 / 起售菜品
     *
     * @param status 0 - 停售 / 1 - 起售
     * @param ids    目标菜品
     * @return
     */
    @Override
    public Boolean enableOrDisableSetmeal(Integer status, String ids) {

        UpdateWrapper<Setmeal> updateWrapper = new UpdateWrapper<>();
        String[] idList = ids.split(",");
        // 单个操作
        if (idList.length == 1) {
            if (status == 1) {

                updateWrapper.set("status", 1).eq("id", ids);

                setmealMapper.update(null, updateWrapper);
                if (setmealMapper.update(null, updateWrapper) > 0) {
                    return true;
                }
            }
            // 停售
            if (status == 0) {
                updateWrapper.set("status", 0).eq("id", ids);
                if (setmealMapper.update(null, updateWrapper) > 0) {
                    return true;
                }

            }
        }
        // 批量操作
        // 起售
        if (status == 1) {
            updateWrapper.set("status", 1).in("id", idList);
            setmealMapper.update(null, updateWrapper);
            if (setmealMapper.update(null, updateWrapper) > 0) {
                return true;
            }
        }
        // 停售
        if (status == 0) {
            updateWrapper.set("status", 0).in("id", idList);
            setmealMapper.update(null, updateWrapper);
            if (setmealMapper.update(null, updateWrapper) > 0) {
                return true;
            }
        }
        return false;

    }

    @Override
    public Boolean delSetmealById(String ids) {
        String[] idList = ids.split(",");
        //  单删
        if (idList.length == 1) {
            if (setmealMapper.deleteById(idList[0]) > 0) {
                return true;
            }
        }

        // 批量删除
        if (setmealMapper.deleteBatchIds(Arrays.asList(idList)) > 0) {
            return true;
        }

        return false;
    }

    /**
     * 根据 id 查询套餐信息
     */
    @Override
    public SetmealDto getSetmealDtoById(String id) {
        Setmeal setmeal = getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        Long categoryId = setmeal.getCategoryId();
        Category category = categoryMapper.selectById(categoryId);
        setmealDto.setCategoryName(category.getName());
        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setmeal_id", id);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }

    /**
     * 修改菜品信息，同时更改套餐菜品表
     */
    @Override
    public Boolean editSetmeal(SetmealDto setmealDto) {
        // 更改套餐信息
        if (setmealMapper.updateById(setmealDto) <= 0) {
            throw new BusinessException("该套餐不存在");
        }

        // 更改套餐菜品信息
        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setmeal_id", setmealDto.getId());
        setmealDishMapper.delete(queryWrapper);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(String.valueOf(setmealDto.getId()));
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return true;
    }

    /**
     * 根据分类 id 查询套餐信息
     */
    @Override
    public List<Setmeal> getSetmealDishById(String categoryId) {
        // 1. 通过分类 id 获取套餐 id
        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        // 是否起售
        queryWrapper.eq("status", 1);
        List<Setmeal> setmeals = setmealMapper.selectList(queryWrapper);
        return setmeals;
    }
}




