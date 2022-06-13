package com.liantong.demo_part2.Intercepter;

import com.liantong.demo_part2.Entity.User;
import com.mysql.cj.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HandlerInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");
        System.out.println(request.getSession().getId());
        if(user == null ){
            //转发或者重定向？先选择转发
            log.info("请求拦截，跳转到："+"/Access/goLogin");
            request.getRequestDispatcher("/Access/goLogin").forward(request, response);
//            response.sendRedirect( "/goLogin");
            return false;
        }
        return true;
    }
}
