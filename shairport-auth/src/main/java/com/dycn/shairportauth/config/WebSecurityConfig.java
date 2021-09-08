package com.dycn.shairportauth.config;

import com.dycn.shairportauth.service.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Liq
 * @date 2021/3/30
 */
@EnableWebSecurity
@EnableConfigurationProperties(Custom2Config.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    CustomUserDetailsServiceImpl customUserDetailsService;


    @Autowired
    Custom2Config custom2Config;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider ;
    }


    /**
     * 配置认证的用户信息
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置数据库认证
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider()) ;
    }



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        WebSecurity webSecurity = web.ignoring().and();

        // 按照指定规则过滤
        custom2Config.getIgnores().forEach(url -> webSecurity.ignoring().antMatchers(url));
    }


    /**
     * 允许匿名访问所有接口 主要是 oauth 接口
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/oauth/**").permitAll()
                .antMatchers("/webjars/**","/doc.html","/swagger-resources/**","/v2/api-docs").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();

        // 合并不许要拦截的URL地址
//        String[] excludeUrls = ArrayUtils.addAll(SecurityConstant.PATTERN_URLS, permitUrls);
//
//        http
//                .cors()
//                .and()
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers(excludeUrls).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin();

    }


}
