package com.pfl.takeoutfood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfl.takeoutfood.model.domain.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yagam1
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2022-04-26 18:10:48
* @Entity generator.domain.AddressBook
*/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}




