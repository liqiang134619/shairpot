package com.dycn.shairportauth.config;

import com.google.common.collect.Lists;
import io.swagger.annotations.AuthorizationScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Liq
 * @date 2021-9-7
 */
@Configuration
@EnableOpenApi
public class SwaggerConfiguration {


    @Bean(value = "authApi")
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dycn.shairportauth.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(globalRequestParameters())
                .apiInfo(apiInfo());
    }

    private List<RequestParameter> globalRequestParameters() {
        RequestParameterBuilder parameterBuilder = new RequestParameterBuilder().in(ParameterType.HEADER).name("Authorization").required(false).query(param -> param.model(model -> model.scalarModel(ScalarType.STRING)));
        return Collections.singletonList(parameterBuilder.build());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("认证服务API")
                .description("认证服务api文档API")
                .termsOfServiceUrl("http://127.0.0.1:8200/")
                .version("1.0")
                .build();
    }

}
