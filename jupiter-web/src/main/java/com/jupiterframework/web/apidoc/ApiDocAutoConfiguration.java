package com.jupiterframework.web.apidoc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import com.jupiterframework.constant.CoreConstant;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@Configuration
@ConditionalOnWebApplication
public class ApiDocAutoConfiguration {
    @Autowired(required = false)
    private ApiInfo apiInfo;
    @Autowired
    private ConfigurableEnvironment env;

    @Value("${swagger.enable:true}")
    private boolean enable;


    @Bean
    public Docket getApi() {
        List<Parameter> pars = new ArrayList<>();
        pars.add(new ParameterBuilder().name(CoreConstant.SESSION_KEY).description("Session").defaultValue("")
            .modelRef(new ModelRef("string")).parameterType("header").required(false).build());

        Docket docket = new Docket(DocumentationType.SWAGGER_2).groupName("Default")
            .genericModelSubstitutes(DeferredResult.class).useDefaultResponseMessages(false)
            .forCodeGeneration(true).apiInfo(this.apiInfo == null ? apiInfo() : this.apiInfo).select()
            .apis(RequestHandlerSelectors.basePackage("com.jupiter"))
            // .paths(PathSelectors.regex(".*/jupiter/.*"))
            // .apis(RequestHandlerSelectors.withClassAnnotation(MicroService.class))
            .build().globalOperationParameters(pars)
            .globalResponseMessage(RequestMethod.POST, new ArrayList<>());

        if (!enable)
            docket.enable(false);

        return docket;

    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(this.env.getProperty("spring.application.name") + "的API文档")
            .description("自动生成的文档").termsOfServiceUrl("http://www.jupiter.com").version("1.0").build();
    }

}
