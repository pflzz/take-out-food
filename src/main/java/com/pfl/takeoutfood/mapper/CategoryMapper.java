package com.pfl.takeoutfood.mapper;

import com.pfl.takeoutfood.model.domain.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yagam1
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2022-04-26 18:26:07
* @Entity generator.domain.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




