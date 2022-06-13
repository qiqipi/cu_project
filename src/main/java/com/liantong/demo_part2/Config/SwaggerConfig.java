package com.liantong.demo_part2.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2//开启在线接口文档
public class SwaggerConfig {
	/**
	 * 
	 * 添加摘要信息
	 */
	 @Bean
	    public Docket createRestApi() {
	        return new Docket(DocumentationType.SWAGGER_2)
	        		 .apiInfo(apiInfo())
	                 .select()
	                 .apis(RequestHandlerSelectors.basePackage("com.liantong.demo_part2.Controller"))
	                 .paths(PathSelectors.any())
	                 .build();


	    }


	    private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
					.title("联通接入网后期部分java接口文档Api")
	                .description("描述：用于测试目前已完成的接口、并且规范数据交互格式。其中不需要测试的接口已经进行标注。")
	                .termsOfServiceUrl("")
	                .contact(new Contact("tension", null, null))
	                .version("1.0")
	                .build();
	    }

}
