package com.pfl.takeoutfood.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pfl.takeoutfood.model.domain.Category;

import java.util.List;

/**
* @author yagam1
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2022-04-26 18:26:07
*/
public interface CategoryService extends IService<Category> {

    /**
     * 分页获取分类信息
     * @param PageNo
     * @param PageSize
     * @return
     */
    Page<Category> getCategoryList(int PageNo, int PageSize);

    /**
     * 添加分类
     * @param name 分类种类
     * @param type 分类名称
     * @param sort 分类序号
     * @return
     */
    Boolean addCategory(String name, int type, int sort);


    /**
     * 删除分类
     * @param id
     * @return
     */
    Boolean delCategory(long id);

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    Boolean editCategory(Category category);

    /**
     * 获取 菜品 / 套餐 分类列表
     * @param type 选择 菜品 / 套餐
     * @return
     */
    List<Category> getCategoryList(Integer type);
}
