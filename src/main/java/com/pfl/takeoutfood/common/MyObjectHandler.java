package com.pfl.takeoutfood.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * 自定义元数据对象处理器
 */

@Component
@Slf4j
public class MyObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        long curId = Thread.currentThread().getId();
        log.info("当前线程id: {}", curId);

        //获取当前线程对应的线程局部变量的值（员工 / 用户 id）
        Long id = BaseContext.getId();


        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", id);
        metaObject.setValue("updateUser", id);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        long curId = Thread.currentThread().getId();
        log.info("当前线程id: {}", curId);

        //获取当前线程对应的线程局部变量的值（员工 / 用户 id）
        Long id = BaseContext.getId();


        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", id);
    }
}
