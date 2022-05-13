package com.pfl.takeoutfood.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pfl.takeoutfood.common.BaseResponse;
import com.pfl.takeoutfood.model.domain.Employee;

import javax.servlet.http.HttpServletRequest;

/**
* @author yagam1
* @description 针对表【employee(员工信息)】的数据库操作Service
* @createDate 2022-04-26 18:26:54
*/
public interface EmployeeService extends IService<Employee> {

     /**
      * 员工登录
      * @param username 员工用户名
      * @param password 员工密码
      * @return 用户id
      */
     Employee employeeLogin(String username, String password);

     /**
      * 添加用户
      * @param employee
      * @return
      */
     Boolean addEmployee(Employee employee, HttpServletRequest request);

     /**
      * 分页查询员工信息
      * @param pageNo
      * @param PageSize
      * @param name
      * @return
      */
     BaseResponse<Page> EmployeeList(int pageNo, int PageSize, String name);


}
