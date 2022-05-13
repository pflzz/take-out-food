package com.pfl.takeoutfood.common;


/**
 * ThreadLocal 工具类，保证线程上下文的一致性
 */
public class BaseContext {
    private static ThreadLocal threadLocal = new ThreadLocal();


    public static void setId(Long id) {
        threadLocal.set(id);
    }

    public static Long getId() {
        return (Long) threadLocal.get();
    }

}
