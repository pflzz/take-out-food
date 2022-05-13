package com.pfl.takeoutfood.mapper;

import com.pfl.takeoutfood.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yagam1
* @description 针对表【user(用户信息)】的数据库操作Mapper
* @createDate 2022-04-26 18:27:11
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




