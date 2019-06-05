package com.jupiterframework.workflow;

import java.io.IOException;

import javax.sql.DataSource;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.activiti.spring.boot.ActivitiProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.jupiterframework.threadpool.EnableThreadPoolExecutor;
import com.jupiterframework.workflow.service.impl.ActivitiIDGenerator;


@Configuration
@ComponentScan
@EnableThreadPoolExecutor
@EnableConfigurationProperties(ActivitiProperties.class)
public class WorkFlowAutoConfiguration extends AbstractProcessEngineAutoConfiguration {
    private static final String FONT_NAME = "宋体";


    @Bean
    @ConditionalOnMissingBean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager transactionManager,
            SpringAsyncExecutor springAsyncExecutor, ActivitiIDGenerator activitiIDGenerator) throws IOException {

        SpringProcessEngineConfiguration spe = this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);
        spe.setActivityFontName(FONT_NAME);
        spe.setLabelFontName(FONT_NAME);
        spe.setAnnotationFontName(FONT_NAME);
        spe.setIdGenerator(activitiIDGenerator);
        return spe;
    }

}
