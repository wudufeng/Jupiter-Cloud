package test.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jupiterframework.threadpool.EnableThreadPoolExecutor;


@SpringBootApplication
@EnableThreadPoolExecutor
public class TestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestServiceApplication.class, args);
	}
}
