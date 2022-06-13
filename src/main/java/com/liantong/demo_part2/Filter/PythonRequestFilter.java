package com.liantong.demo_part2.Filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@Order(2)
@WebFilter(filterName = "pythonRequestFilter", urlPatterns = "/Access/api/*")
public class PythonRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String[] templist = ((HttpServletRequest) servletRequest).getRequestURI().split("Access");
//        System.out.println(templist[1]+"1111111111111");
//        System.out.println(((HttpServletRequest) servletRequest).getQueryString()+"22222222222222");
        System.out.println(Arrays.toString(templist));
        String pythonSuffix = templist[1]+"?"+((HttpServletRequest) servletRequest).getQueryString();
        System.out.println("pythonSuffix :"+pythonSuffix);

        servletRequest.setAttribute("name",pythonSuffix);
//        System.out.println("name in Filter :"+servletRequest.getAttribute("name"));
//        ((HttpServletResponse) servletResponse).sendRedirect("/RequestRedirectToPython");
        servletRequest.getRequestDispatcher("/Access/RequestRedirectToPython").forward(servletRequest,servletResponse);
    }


}
