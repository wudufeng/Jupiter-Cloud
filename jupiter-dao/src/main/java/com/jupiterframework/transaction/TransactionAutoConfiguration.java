package com.jupiterframework.transaction;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TransactionAutoConfiguration {

    @Bean
    @ConditionalOnBean({ DataSource.class })
    public org.springframework.transaction.PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new ListenableDataSourceTransactionManager(dataSource);
    }


    @Bean
    @ConditionalOnClass({ org.springframework.transaction.support.TransactionSynchronizationManager.class })
    public TransactionDetermine transactionDetermineManager() {
        return new TransactionDetermineManager();
    }


    @Bean
    @ConditionalOnMissingClass({ "org.springframework.transaction.support.TransactionSynchronizationManager" })
    public TransactionDetermine disableTransactionDetermine() {
        return new DisableTransactionDetermine();
    }
}
