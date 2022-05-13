package com.pfl.takeoutfood.common;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    //路径匹配器 支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        1. 设置uri白名单: 比如放行登录登出请求，静态资源请求
            String[] whiteRequestUrl = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/frontend/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
//        2. 放行白名单中的uri
        if (checkUri(whiteRequestUrl, request.getRequestURI())) {
            log.info("本次请求{}不需要处理", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
//        3. 查找 session 中的登录态，判断用户是否登录
//        4.2 如果后台员工已登录则放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("员工{}已登录", request.getSession().getAttribute("employee"));

            long id = Thread.currentThread().getId();
            log.info("当前线程id: {}", id);

            BaseContext.setId((Long) request.getSession().getAttribute("employee"));

            filterChain.doFilter(request, response);
            return;
        }

        // 4.1 如果前台用户已登录则放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户{}已登录", request.getSession().getAttribute("user"));

            long id = Thread.currentThread().getId();
            log.info("当前线程id: {}", id);

            BaseContext.setId((Long) request.getSession().getAttribute("user"));

            filterChain.doFilter(request, response);
            return;
        }
        log.info("未登录");
//        5. 如果未登录则给后端发送数据，让其重定向到 login.html
        response.getWriter().write(JSON.toJSONString(BaseResponse.error("NOTLOGIN")));
    }

    //检查uri是否在白名单中
    public boolean checkUri(String[] whiteRequestUrl, String requestUri) {
        for (String url : whiteRequestUrl) {
            if (PATH_MATCHER.match(url, requestUri)) {
                return true;
            }
        }
        return false;
    }
}
