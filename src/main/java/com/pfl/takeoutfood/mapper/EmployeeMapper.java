package com.pfl.takeoutfood.mapper;

import com.pfl.takeoutfood.model.domain.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yagam1
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2022-04-26 18:26:54
* @Entity generator.domain.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




