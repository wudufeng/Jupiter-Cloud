package com.jupiter.cloud.upms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author jupiter
 */
@MapperScan(basePackages = "com.jupiter.cloud.upms.**.dao")
@SpringBootApplication
public class JupiterUpmsApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(JupiterUpmsApplication.class);
        springApplication.setBannerMode(Banner.Mode.LOG);
        springApplication.run(args);
    }

}
