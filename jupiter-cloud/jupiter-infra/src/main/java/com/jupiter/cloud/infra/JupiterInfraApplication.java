package com.jupiter.cloud.infra;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jupiter
 */
@MapperScan(basePackages = "com.jupiter.cloud.infra.**.dao")
@SpringBootApplication
public class JupiterInfraApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(JupiterInfraApplication.class);
        springApplication.setBannerMode(Banner.Mode.LOG);
        springApplication.run(args);
    }
}

