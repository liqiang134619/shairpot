package com.dycn.shairportauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author Liq
 * @date 2021/3/30
 */
@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {



    @Override
    public void configure(HttpSecurity http) throws Exception {
        //允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
        http.headers().frameOptions().disable()
                .and()
                .requestMatcher(request -> false)
                .authorizeRequests()
                // 配置一些直接过滤的接口
//                .antMatchers(securityProperties.getIgnore().getUrls()).permitAll()
                .anyRequest()
                .authenticated();
    }
}
