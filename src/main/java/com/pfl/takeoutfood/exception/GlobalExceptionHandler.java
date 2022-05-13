package com.pfl.takeoutfood.exception;

import com.pfl.takeoutfood.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class}) // 需要通知的controller
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 处理 对表中 unique 字段 重复添加的异常
     * @param e
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public BaseResponse<String> exceptionHandler(SQLIntegrityConstraintViolationException e) {
        /**
         * 处理重复添加员工的异常
         */
        log.info(e.getMessage());
        if (e.getMessage().startsWith("Duplicate")) {
            String[] s = e.getMessage().split(" ");
            String msg = s[2] + "已被注册";
            return BaseResponse.error(msg);
        }
        return BaseResponse.error("未知错误");
    }


    /**
     * 处理自定义业务异常信息
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<String> exceptionHandler(BusinessException e) {
        log.info(e.getMessage());
        return BaseResponse.error(e.getMessage());
    }
}
