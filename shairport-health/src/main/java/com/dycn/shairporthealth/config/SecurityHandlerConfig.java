package com.dycn.shairporthealth.config;

import com.dycn.shairportcommon.constant.Status;
import com.dycn.shairportcommon.util.ResponseUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Liq
 * @date 2021-9-8
 */

@Component
@Configuration
public class SecurityHandlerConfig {



    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            ResponseUtil.renderJson(response, Status.ACCESS_DENIED);
        };
    }

    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }


    /**
     * 处理spring security oauth 处理失败返回消息格式
     * code
     * resp_desc
     */
    @Bean
    public OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler(){
        return new OAuth2AccessDeniedHandler(){

            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException, ServletException {
                ResponseUtil.renderJson(response, Status.ACCESS_DENIED);
            }
        };
    }



}
