package com.jupiterframework.autoconfig.mybatis.generator;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnWebApplication
@ConditionalOnBean({ DataSource.class })
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class GeneratorAutoConfiguration {

	@Bean
	public GeneratorController generatorProcessor(DataSource dataSource) {
		return new GeneratorController(new GeneratorProcessor(new DatabaseMetaDataProvider(dataSource)));
	}

}
