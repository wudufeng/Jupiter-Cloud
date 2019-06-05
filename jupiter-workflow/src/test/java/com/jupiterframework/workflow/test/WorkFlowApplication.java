package com.jupiterframework.workflow.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(
// exclude = {
// org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
// org.activiti.spring.boot.SecurityAutoConfiguration.class }
// ,org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration.class
)
public class WorkFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkFlowApplication.class, args);
    }

}
