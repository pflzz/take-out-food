package com.pfl.takeoutfood.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pfl.takeoutfood.model.domain.Category;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class CategoryServiceTest {

    @Resource
    private CategoryService categoryService;

    @Test
    void testGetList() {
        int pageNo = 1;
        int pageSize = 5;
        Page<Category> categoryList = categoryService.getCategoryList(pageNo, pageSize);
        List<Category> records = categoryList.getRecords();
        System.out.println(records.size());
    }
}
