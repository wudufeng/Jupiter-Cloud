package com.ueb.framework.channel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ChannelApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChannelApplication.class, args);

    }


    @Override
    public void run(String... args) throws Exception {

    }
}
