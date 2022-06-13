package com.liantong.demo_part2.Intercepter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器无用，已用过滤器代替。
 */
//@Component
public class PythonFuncIntercepter implements HandlerInterceptor {

    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("拦截器被使用");
        String []templist = request.getRequestURI().split("api/");
        String pythonSuffix = "/api/"+templist[1];
        response.sendRedirect( "/RequestRedirectToPython?pythonSuffix="+pythonSuffix);
        return false;
    }
}
