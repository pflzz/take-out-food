package com.pfl.takeoutfood.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pfl.takeoutfood.common.BaseResponse;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.model.domain.User;
import com.pfl.takeoutfood.service.UserService;
import com.pfl.takeoutfood.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;


    /**
     * 获取手机验证码
     * @param user 请求验证码的用户
     * @return
     */
    @PostMapping("/sendMsg")
    public BaseResponse<String> sendMsg(@RequestBody User user, HttpServletRequest request) {

        if (user == null) {
            throw new BusinessException("参数错误");
        }

        // 1. 获取手机号
        String phone = user.getPhone();

        // 2. 生成4位的验证码
        String code = ValidateCodeUtils.generateValidateCode4String(4);
        log.info("验证码 = {}", code);

        // 3. 使用阿里云给用户发送短信
        // SMSUtils.sendMessage("", "", phone, code);

        // 4. 把验证码存入 session
        HttpSession session = request.getSession();
        session.setAttribute("code", code);

        return BaseResponse.success("短信发送成功");
    }


    /**
     * 验证码登录
     * @param map 用户的手机号和输入的验证码
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody Map map, HttpServletRequest request) {
        // 获取信息
        String code = map.get("code").toString();
        String phone = map.get("phone").toString();

        // 1. 从 Session 获取验证码
        HttpSession session = request.getSession();
        String sessionCode = session.getAttribute("code").toString();

        // 2. 进行比对, 如果能比对成功，说明登录成功
        if (sessionCode != null && sessionCode.equals(code)) {
            // 4. 判断当前手机号对应的用户是否为新用户
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);
            User user = userService.getOne(queryWrapper);

            // 如果是新用户就自动完成注册
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }

            session.setAttribute("user", user.getId());
            return BaseResponse.success(user);
        }

        return BaseResponse.error("登陆失败");
    }
}
