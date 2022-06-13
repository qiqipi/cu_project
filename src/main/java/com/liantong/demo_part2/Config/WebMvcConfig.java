package com.liantong.demo_part2.Config;
import com.liantong.demo_part2.Intercepter.AdminInterceptor;
import com.liantong.demo_part2.Intercepter.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport{

	//配置静态资源映射
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 解决静态资源无法访问
//		registry.addResourceHandler("/index.html")
//				.addResourceLocations("classpath:/static/");
		/**
		 * 有时候静态资源依旧无法加载可能是由于target目录没有更新，新放进去的静态资源文件没有放置在target中。
		 * 因此设置好静态资源路径后，记得rebuild一下项目，即可。
		 **/
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/");
		// 解决swagger无法访问
		registry.addResourceHandler("/swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		// 解决swagger的js文件无法访问
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
		System.out.println("swagger静态资源映射成功");
	}

	//配置跨域
	 @Override
	 public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	                .allowedOrigins("*")
	                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
	                .allowCredentials(true).maxAge(3600);
	 }
	 
	 private CorsConfiguration corsConfig() {
		    CorsConfiguration corsConfiguration = new CorsConfiguration();
//		    /* 请求常用的三种配置，*代表允许所有，当时你也可以自定义属性（比如header只能带什么，只能是post方式等等）
//		    */
		    corsConfiguration.addAllowedOrigin("*");
		    corsConfiguration.addAllowedHeader("*");
		    corsConfiguration.addAllowedMethod("*");
		    corsConfiguration.setAllowCredentials(true);
		    corsConfiguration.setMaxAge(3600L);
		    return corsConfiguration;
		}
	 
		@Bean
		public CorsFilter corsFilter() {
		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		    source.registerCorsConfiguration("/**", corsConfig());
		    return new CorsFilter(source);
		}

	/**
	 * 拦截器顺序如何配置？
	 * 注册时的先后就是执行的顺序
	 * 过滤器则不同，@Order标签不起作用，跟过滤器首字母顺序有关
 	 * @param registry
	 */
//	@Override
//	protected void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/Access/RequestRedirectToPython")
//				.addPathPatterns("/Access/firstTest")
//				.addPathPatterns("/Access/test3")
//				.addPathPatterns("/Access/user/*");
//		registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/Access/user/*");
//		super.addInterceptors(registry);
//	}

}
