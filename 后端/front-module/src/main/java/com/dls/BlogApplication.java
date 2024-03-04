package com.dls;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
// 在启动类上配置我们的Mapper在哪个包。
@MapperScan("com.dls.dao")
// @EnableScheduling是spring提供的定时任务的注解
@EnableScheduling
@EnableSwagger2
// 作为引导类
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class,  args);
    }
}
