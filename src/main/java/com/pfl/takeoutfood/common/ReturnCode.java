package com.pfl.takeoutfood.common;

/**
 * 定义状态码
 */
public enum ReturnCode {
    /**
     * 操作成功
     */
    RC100(1, "操作成功"),
    /**
     * 操作失敗
     */
    RC999(999, "操作失败");

    //todo 添加更多的状态码信息

    /**
     * 自定义状态码
     */
    private final Integer code;
    private final String message;

    ReturnCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
