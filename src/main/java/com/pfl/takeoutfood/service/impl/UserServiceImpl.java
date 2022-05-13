package com.pfl.takeoutfood.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pfl.takeoutfood.mapper.UserMapper;
import com.pfl.takeoutfood.model.domain.User;
import com.pfl.takeoutfood.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author yagam1
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-04-26 18:27:11
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




