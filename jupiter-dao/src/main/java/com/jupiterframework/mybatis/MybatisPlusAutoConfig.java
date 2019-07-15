package com.jupiterframework.mybatis;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisMapperRefresh;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.jupiterframework.dao.GenericDao;
import com.jupiterframework.mybatis.generator.CodeGeneratorController;

@Configuration
@ConditionalOnBean({ DataSource.class })
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({ MybatisExtendProperties.class })
@MapperScan(basePackages = "com.jupiterframework.**.dao", markerInterface = GenericDao.class)
public class MybatisPlusAutoConfig {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MybatisProperties properties;
    @Autowired
    private MybatisExtendProperties extProperties;
    @Autowired
    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private String[] deafultMapperLocations = { "classpath*:mapper/**/*.xml", "classpath*:config/mapper/**/*.xml" };

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType(this.extProperties.getDbType());
        return page;
    }

    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(@Autowired(required = false) DatabaseIdProvider databaseIdProvider, Interceptor[] interceptors) {
        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(this.dataSource);
        mybatisPlus.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.properties.getConfigLocation())) {
            mybatisPlus.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        }
        // if (!ObjectUtils.isEmpty(this.interceptors)) {
        mybatisPlus.setPlugins(interceptors);
        // }
        if ((this.properties.getMapperLocations() == null) || (this.properties.getMapperLocations().length < 1)) {
            this.properties.setMapperLocations(this.deafultMapperLocations);
        }

        GlobalConfiguration globalConfig = new GlobalConfiguration();
        globalConfig.setDbType(this.extProperties.getDbType());
        globalConfig.setIdType(this.extProperties.getIdType());
        globalConfig.setDbColumnUnderline(this.extProperties.isDbColumnUnderline());
        globalConfig.setSqlInjector(new CustomSqlInjector());
        mybatisPlus.setGlobalConfig(globalConfig);

        MybatisConfiguration mc = new MybatisConfiguration();
        if (this.properties.getConfiguration() != null) {
            BeanUtils.copyProperties(this.properties.getConfiguration(), mc);
        }
        mc.setMapUnderscoreToCamelCase(true);
        mc.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);

        mybatisPlus.setConfiguration(mc);
        if (databaseIdProvider != null) {
            mybatisPlus.setDatabaseIdProvider(databaseIdProvider);
        }
        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
            mybatisPlus.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
            mybatisPlus.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
            mybatisPlus.setMapperLocations(this.properties.resolveMapperLocations());
        }
        return mybatisPlus;
    }

    @Bean
    @Autowired
    public MybatisMapperRefresh mybatisMapperRefresh(SqlSessionFactory sqlSessionFactory) {
        if ((this.properties.getMapperLocations() == null) || (this.properties.getMapperLocations().length < 1)) {
            this.properties.setMapperLocations(this.deafultMapperLocations);
        }
        MybatisExtendProperties.ReloadPropertis conf = this.extProperties.getReloadMapper();
        return new MybatisMapperRefresh(this.properties.resolveMapperLocations(), sqlSessionFactory, conf.getDelaySecond(), conf.getSleepSeconds(), conf.isEnabled());
    }

    @ConditionalOnWebApplication
    @Bean
    public CodeGeneratorController generatorController() {
        return new CodeGeneratorController();
    }
}
