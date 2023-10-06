package com.jupiter.cloud.auth;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author jupiter
 */
@SpringBootApplication
public class JupiterAuthApplication {

	public static void main(String[] args) {
		SpringApplication app  = new SpringApplication(JupiterAuthApplication.class);
		app.setBannerMode(Banner.Mode.LOG);
		app.run(args);
	}
}
