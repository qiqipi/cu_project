package com.liantong.demo_part2.Filter;

import org.springframework.core.annotation.Order;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;

@Order(1)
@WebFilter(filterName = "a_RequestLogFilter", urlPatterns = "/*")
public class A_RequestLogFilter implements Filter {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(A_RequestLogFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

//        HttpServletRequest request1 = (HttpServletRequest) servletRequest;
//        HttpSession session = request1.getSession();
//        String user=(String)session.getAttribute("user");
//        if (user == null) {
//            session.setAttribute("user","666");
//            System.out.println("设置session并转发");
//            servletRequest.getRequestDispatcher("/Access/test5").forward(servletRequest,servletResponse);
//        }
        StringBuffer request = ((HttpServletRequest) servletRequest).getRequestURL();
//        String uri = ((HttpServletRequest) servletRequest).getRequestURI();
//        StringBuffer url = ((HttpServletRequest) servletRequest).getRequestURL();
//        System.out.println("URI"+uri);
//        System.out.println("URL"+url);
        String back = ((HttpServletRequest) servletRequest).getQueryString();
//        System.out.println(back);
        if (back != null){
            String totaltequest = request +"?"+back;
//            log.info("用户请求操作为："+totaltequest);
            System.out.println("RequestLogFilter: "+totaltequest);
        }
        else {
            log.info("用户请求操作为："+request);
            System.out.println("RequestLogFilter: "+request);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}