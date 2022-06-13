//package com.liantong.demo_part2.Config;
//
//import com.liantong.demo_part2.Intercepter.LoginInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
////@Configuration
//public class InterceptorConfig extends WebMvcConfigurationSupport {
//
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        System.out.println("注册拦截器成功");
//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/RequestRedirectToPython")
//                .addPathPatterns("/firstTest")
//                .addPathPatterns("/test3");
//
//        super.addInterceptors(registry);
//    }
//
//
//
//}
