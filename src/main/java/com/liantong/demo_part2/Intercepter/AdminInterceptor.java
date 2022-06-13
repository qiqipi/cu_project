package com.liantong.demo_part2.Intercepter;

import com.liantong.demo_part2.Entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null || !"y".equals(user.getIs_admin()) ){
            //转发或者重定向？先选择转发
            request.getRequestDispatcher("/Access/NotAdmin").forward(request, response);
            System.out.println("被拦截");
//            response.sendRedirect( "/NotAdmin");
            return false;
        }
        return true;
    }
}
