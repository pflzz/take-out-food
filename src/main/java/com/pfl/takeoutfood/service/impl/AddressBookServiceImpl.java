package com.pfl.takeoutfood.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pfl.takeoutfood.mapper.AddressBookMapper;
import com.pfl.takeoutfood.model.domain.AddressBook;
import com.pfl.takeoutfood.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
* @author yagam1
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-04-26 18:10:48
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService {

}




