package com.pfl.takeoutfood.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pfl.takeoutfood.mapper.DishFlavorMapper;
import com.pfl.takeoutfood.model.domain.DishFlavor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class DishFlavorServiceTest {
    @Resource
    private DishFlavorMapper dishFlavorMapper;
    @Test
    public void testDemo() {
        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dish_id", "1523274279609253889");
        int result = dishFlavorMapper.delete(queryWrapper);
        System.out.println(result);
    }

}
