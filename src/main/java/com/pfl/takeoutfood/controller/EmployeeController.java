package com.pfl.takeoutfood.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pfl.takeoutfood.common.BaseResponse;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.model.domain.Employee;
import com.pfl.takeoutfood.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 员工登录接口
     *
     * @param request
     * @return 员工 id
     */
    @PostMapping("/login")
    public BaseResponse<Employee> employeeLogin(@RequestBody Employee employee, HttpServletRequest request) {
        if (employee == null) {
            return BaseResponse.error("参数错误");
        }

        Employee curEmp = employeeService.employeeLogin(employee.getUsername(), employee.getPassword());
        if (curEmp == null) {
            return BaseResponse.error("账户不存在或密码错误");
        }
        request.getSession().setAttribute("employee", curEmp.getId());
        return BaseResponse.success(curEmp);
    }

    /**
     * 员工注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<String> employeeLogout(HttpServletRequest request) {
        if (request == null) {
            return BaseResponse.error("参数错误");
        }
        request.getSession().removeAttribute("employee");
        return BaseResponse.success("null");
    }

    /**
     * 添加员工
     *
     * @param employee 要新增员工的信息
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse<String> addEmployee(@RequestBody Employee employee, HttpServletRequest request) {
        if (employee == null) {
            throw new BusinessException("参数错误");
        }

        if (employeeService.addEmployee(employee, request)) {
            return BaseResponse.success(null);
        }
        return BaseResponse.error("添加失败");
    }

    /**
     * 员工信息分页查询
     *
     * @param page     查询的目标页数
     * @param pageSize 每页的数据量
     * @param name     搜索的关键词
     * @return
     */
    @GetMapping("/page")
    public BaseResponse<Page> employeeList(int page, int pageSize, String name) {
        return employeeService.EmployeeList(page, pageSize, name);
    }

    /**
     * 根据id修改员工信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public BaseResponse<String> enableOrDisableEmployee(@RequestBody Employee employee) {


        if (employeeService.updateById(employee)) {
            return BaseResponse.success("操作成功");
        }


        return BaseResponse.error("操作失败");
    }

    @GetMapping("/{id}")
    public BaseResponse<Employee> getById(@PathVariable Long id) {

        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return BaseResponse.success(employee);
        }

        return BaseResponse.error("查询失败");
    }
}
