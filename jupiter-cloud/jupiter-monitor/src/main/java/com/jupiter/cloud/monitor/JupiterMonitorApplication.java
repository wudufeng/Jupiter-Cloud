package com.jupiter.cloud.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author jupiter
 */
@EnableAdminServer
@SpringBootApplication
public class JupiterMonitorApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(JupiterMonitorApplication.class);
        app.setBannerMode(Banner.Mode.LOG);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}
