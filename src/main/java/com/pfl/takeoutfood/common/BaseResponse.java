package com.pfl.takeoutfood.common;

import lombok.Data;

import java.util.HashMap;

/**
 * 响应给前端的消息实体
 *
 * @author yagam1
 */
@Data
public class BaseResponse<T> {
    //状态码
    private Integer code;
    //消息
    private String msg;
    //响应数据
    private T data;
    //时间戳
    private long timestamp;

    private HashMap hashMap;

    public BaseResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    //成功
    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> result = new BaseResponse<>();
        result.setCode(ReturnCode.RC100.getCode());
        result.setMsg(ReturnCode.RC100.getMessage());
        result.data = data;
        return result;
    }

    //失败
    public static <T> BaseResponse<T> error(String msg) {
        BaseResponse<T> result = new BaseResponse<>();
        result.msg = msg;
        result.code = 0;
        return result;
    }
}
