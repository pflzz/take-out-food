package com.pfl.takeoutfood.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pfl.takeoutfood.common.BaseResponse;
import com.pfl.takeoutfood.model.domain.Category;
import com.pfl.takeoutfood.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;


    /**
     * 分页获取分类信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public BaseResponse<Page> getCategoryList(Integer page, Integer pageSize) {

        if (page == null || pageSize == null) {
            return BaseResponse.error("参数错误");
        }

        return BaseResponse.success(categoryService.getCategoryList(page, pageSize));
    }


    /**
     * 添加分类
     * @return
     */
    @PostMapping
    public BaseResponse<String> addCategory(@RequestBody Category category) {

        if (category == null) {
            return BaseResponse.error("参数错误");
        }

       if (categoryService.addCategory(category.getName(), category.getType(), category.getSort())) {
           return BaseResponse.success("添加成功");
       }
       return BaseResponse.error("添加失败");
    }

    /**
     * 根据id 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public BaseResponse<String> delCategory(Long id) {
        if (id == null) {
            return BaseResponse.error("参数错误");
        }
        if (categoryService.delCategory(id)) {
            return BaseResponse.success("删除成功");
        }
        return BaseResponse.error("删除失败");
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public BaseResponse<String> editCategory(@RequestBody Category category) {
        if (category == null) {
            return BaseResponse.error("参数错误");
        }
        if (categoryService.editCategory(category)) {
            return BaseResponse.success("修改成功");
        }
        return BaseResponse.error("修改失败");
    }

    /**
     * 获取 菜品 / 套餐 分类列表
     * @param type 选择 菜品 / 套餐
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<Category>> getCategoryList(Integer type) {


        List<Category> categoryList = categoryService.getCategoryList(type);
        return BaseResponse.success(categoryList);
    }


}
