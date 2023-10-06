package com.jupiter.cloud.gateway;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author jupiter
 */
@SpringBootApplication
public class JupiterGatewayApplication {

    public static void main(String[] args) {
        SpringApplication app  = new SpringApplication(JupiterGatewayApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.setBannerMode(Banner.Mode.LOG);
        app.run(args);
    }

}
