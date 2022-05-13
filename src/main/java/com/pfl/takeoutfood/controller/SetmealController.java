package com.pfl.takeoutfood.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pfl.takeoutfood.common.BaseResponse;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.model.domain.Setmeal;
import com.pfl.takeoutfood.model.dto.SetmealDto;
import com.pfl.takeoutfood.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {


    @Resource
    private SetmealService setmealService;


    /**
     * 新增套餐
     */
    @PostMapping
    public BaseResponse<String> addSetmealDto(@RequestBody SetmealDto setmealDto) {

        if (setmealDto == null) {
            throw new BusinessException("参数错误");
        }
        if (setmealService.addSetmealDto(setmealDto)) {
            return BaseResponse.success("添加成功");
        }
        return BaseResponse.error("添加失败");
    }


    /**
     * 分页获取套餐信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public BaseResponse<Page> getSetmealList(Integer page, Integer pageSize, String name) {
        if (page == null || pageSize == null) {
            throw new BusinessException("参数错误");
        }
        Page pageInfo = setmealService.getSetmealList(page, pageSize, name);
        return BaseResponse.success(pageInfo);
    }

    /**
     * 根据 id 单独 / 批量 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public BaseResponse<String> delDishById(String ids) {
        if (ids == null) {
            throw new BusinessException("参数错误");
        }

        if (setmealService.delSetmealById(ids)) {
            return BaseResponse.success("删除成功");
        }
        throw new BusinessException("此套餐不存在，删除失败");
    }

    /**
     * 批量 / 单个 停售 / 起售套餐
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
        if (setmealService.enableOrDisableSetmeal(status, ids)) {
            return BaseResponse.success("操作成功");
        }
        return BaseResponse.error("操作失败");
    }

    /**
     * 根据 id 查询套餐信息
     */
    @GetMapping("/{id}")
    public BaseResponse<SetmealDto> getDishById(@PathVariable String id) {
        if (id == null) {
            throw new BusinessException("参数错误");
        }
        SetmealDto setmealDto = setmealService.getSetmealDtoById(id);
        if (setmealDto == null) {
            throw new BusinessException("该套餐不存在");
        }
        return BaseResponse.success(setmealDto);

    }
    /**
     * 根据分类 id 查询套餐信息
     */
    @GetMapping("/list")
    public BaseResponse<List<Setmeal>> getSetmealById(Setmeal setmeal) {
        if (setmeal == null) {
            throw new BusinessException("参数错误");
        }
        Long categoryId = setmeal.getCategoryId();
        List<Setmeal> setmeals = setmealService.getSetmealDishById(String.valueOf(categoryId));

        return BaseResponse.success(setmeals);

    }
    /**
     * 根据 id 修改套餐信息，并改变套餐菜品表信息
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    public BaseResponse<String> editDish(@RequestBody SetmealDto setmealDto) {
        if (setmealDto == null) {
            throw new BusinessException("参数错误");
        }
        if (setmealService.editSetmeal(setmealDto)) {

            return BaseResponse.success("修改成功");
        }
        return BaseResponse.error("修改失败");
    }
}
