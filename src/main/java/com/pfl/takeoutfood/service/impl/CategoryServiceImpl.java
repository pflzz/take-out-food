package com.pfl.takeoutfood.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.mapper.CategoryMapper;
import com.pfl.takeoutfood.mapper.DishMapper;
import com.pfl.takeoutfood.mapper.SetmealMapper;
import com.pfl.takeoutfood.model.domain.Category;
import com.pfl.takeoutfood.model.domain.Dish;
import com.pfl.takeoutfood.model.domain.Setmeal;
import com.pfl.takeoutfood.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yagam1
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
 * @createDate 2022-04-26 18:26:07
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private DishMapper dishMapper;
    @Resource
    private SetmealMapper setmealMapper;

    /**
     * 分页获取分类信息
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<Category> getCategoryList(int pageNo, int pageSize) {

        Page<Category> pageInfo = new Page<>(pageNo, pageSize);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        this.page(pageInfo, queryWrapper);
        return pageInfo;
    }


    /**
     * 添加分类
     *
     * @param name 分类种类
     * @param type 分类名称
     * @param sort 分类序号
     * @return
     */
    @Override
    public Boolean addCategory(String name, int type, int sort) {
        Category category = new Category();
        category.setId(null);
        category.setType(type);
        category.setName(name);
        category.setSort(sort);

        // 元数据处理器会自动填充
//        category.setCreateTime();
//        category.setUpdateTime();
//        category.setCreateUser();
//        category.setUpdateUser();


        if (categoryMapper.insert(category) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 删除分类， 在删除前得判断当前分类是否关联了 菜品 / 套餐
     *
     * @param id
     * @return
     */
    @Override
    public Boolean delCategory(long id) {

        QueryWrapper<Dish> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("category_id", id);

        // 判断是否有菜品关联到当前分类
        if (dishMapper.selectCount(queryWrapper1) > 0) {
            // 自定义业务异常
            throw new BusinessException("当前分类关联了菜品，无法删除");
        }
        // 判断是否有套餐关联到当前分类
        QueryWrapper<Setmeal> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("category_id", id);

        // 判断是否有菜品关联到当前分类
        if (setmealMapper.selectCount(queryWrapper2) > 0) {
            // 自定义业务异常
            throw new BusinessException("当前分类关联了套餐，无法删除");
        }
        // 删除当前分类
        if (categoryMapper.deleteById(id) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 修改分类信息
     *
     * @param category
     * @return
     */
    @Override
    public Boolean editCategory(Category category) {

        if (updateById(category)) {
            return true;
        }
        return false;
    }

    /**
     * 获取 菜品 / 套餐 分类列表
     * @param type 选择 菜品 / 套餐
     * @return
     */
    @Override
    public List<Category> getCategoryList(Integer type) {


        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(type != null, "type", type);
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);
        return categoryList;
    }
}




