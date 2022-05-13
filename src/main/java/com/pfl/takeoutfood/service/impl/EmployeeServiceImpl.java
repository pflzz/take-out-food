package com.pfl.takeoutfood.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pfl.takeoutfood.common.BaseResponse;
import com.pfl.takeoutfood.common.ReturnCode;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.mapper.EmployeeMapper;
import com.pfl.takeoutfood.model.domain.Employee;
import com.pfl.takeoutfood.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yagam1
 * @description 针对表【employee(员工信息)】的数据库操作Service实现
 * @createDate 2022-04-26 18:26:54
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {


    @Resource
    private EmployeeMapper employeeMapper;

    /**
     * md5 加密的盐值
     */
    //private static final String SALT = "PFLYYDS";

    /**
     * 员工登录
     *
     * @param username 员工用户名
     * @param password 员工密码
     * @return 用户id
     */
    @Override
    public Employee employeeLogin(String username, String password) {
        //1. 校验
        //账户密码只要有一个为空字符串，返回 null
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException( "参数中含有空字符");
        }

        //账户里是否含有非法字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            return null;
        }

        //2. 查询数据库是否存在该员工
        QueryWrapper queryWrapper = new QueryWrapper<Employee>();
        queryWrapper.eq("username", username);

        //加密密码
        String encryptedPassword = DigestUtils.md5DigestAsHex((password).getBytes());
        queryWrapper.eq("password", encryptedPassword);


        Employee employee = employeeMapper.selectOne(queryWrapper);
        //是否能查询到该用户
        if (employee == null) {
            return null;
        }
        // 该用户状态是否正常
        if (employee.getStatus() == 0) {
            return null;
        }
        //3. 员工信息脱敏
        Employee safetyEmployee = new Employee();
        safetyEmployee.setId(employee.getId());
        safetyEmployee.setName(employee.getName());
        safetyEmployee.setUsername(employee.getUsername());
        safetyEmployee.setPassword(null);
        safetyEmployee.setPhone(employee.getPhone());
        safetyEmployee.setSex(employee.getSex());
        safetyEmployee.setIdNumber(null);
        safetyEmployee.setStatus(null);
        safetyEmployee.setCreateTime(null);
        safetyEmployee.setUpdateTime(null);
        safetyEmployee.setCreateUser(null);
        safetyEmployee.setUpdateUser(null);
        return safetyEmployee;
    }

    /**
     * 添加员工
     *
     * @param employee
     * @return
     */
    @Override
    public Boolean addEmployee(Employee employee, HttpServletRequest request) {

        // 处理员工信息
        Employee curEmp = new Employee();
        curEmp.setId(null);
        curEmp.setName(employee.getName());
        curEmp.setUsername(employee.getUsername());
        curEmp.setPassword(employee.getPassword());
        curEmp.setPhone(employee.getPhone());
        curEmp.setSex(employee.getSex());
        curEmp.setIdNumber(employee.getIdNumber());
        curEmp.setStatus(1);
//        curEmp.setCreateTime(LocalDateTime.now());
//        curEmp.setUpdateTime(LocalDateTime.now());
//        curEmp.setCreateUser((Long) (request.getSession().getAttribute("employee")));
//        curEmp.setUpdateUser((Long) (request.getSession().getAttribute("employee")));


        // 将用户存储到数据库中
        if (employeeMapper.insert(curEmp) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 分页查询员工信息
     *
     * @param pageNo
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public BaseResponse<Page> EmployeeList(int pageNo, int pageSize, String name) {
        // 分页构造器
        Page<Employee> pageInfo = new Page<>(pageNo, pageSize);
        // 关键词查询构造器
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);
        // 添加排序条件
        queryWrapper.orderByAsc("update_time");
        this.page(pageInfo, queryWrapper);
        return BaseResponse.success(pageInfo);
    }


}




