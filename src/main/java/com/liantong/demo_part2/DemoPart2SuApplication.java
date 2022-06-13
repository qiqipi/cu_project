package com.liantong.demo_part2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages= {"com.liantong.demo_part2"})
@MapperScan("com.liantong.demo_part2.Mapper")
@ServletComponentScan
public class DemoPart2SuApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoPart2SuApplication.class, args);
    }

}
