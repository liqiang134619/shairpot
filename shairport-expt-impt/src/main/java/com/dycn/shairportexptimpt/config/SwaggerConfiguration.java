package com.dycn.shairportexptimpt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

/**
 * @author Liq
 * @date 2021-9-7
 */
@Configuration
@EnableOpenApi
public class SwaggerConfiguration {


    @Bean(value = "export-import-api")
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dycn.shairportexptimpt.config.controller"))
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
                .title("export-import-API")
                .description("export-import服务API")
                .termsOfServiceUrl("http://127.0.0.1:xxxx/")
                .version("1.0")
                .build();
    }

}
