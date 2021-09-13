package com.dycn.shairportauth.config;

import com.dycn.shairportauth.service.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Configuration
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


    /**
     *  配置认证
     * @param auth 授权断点
     * @throws Exception 认证失败
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置数据库认证
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

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



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors()
                // 关闭 CSRF
                .and().csrf().disable()
                // 登录行为由自己实现，参考 AuthController#login
                .formLogin().disable()
                .httpBasic().disable()    // 认证请求
                .authorizeRequests()
                .antMatchers("/oauth/**", "/login/**", "/logout/**", "/**", "/v2/**")
                .permitAll()
                // 所有请求都需要登录访问
                .anyRequest()
                .authenticated()
                .and()
                .headers().frameOptions().disable();

    }















    /******************************************************************************************************/

    //    @Autowired
//    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(daoAuthenticationProvider()) ;
//    }

    //    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        return daoAuthenticationProvider ;
//    }


//        http
//                .authorizeRequests().antMatchers("/oauth/**").permitAll()
//                .antMatchers("/webjars/**", "/doc.html", "/swagger-resources/**", "/v2/api-docs").permitAll()
//                .anyRequest().permitAll()
//                .and()
//                .csrf().disable()
//                .formLogin()
//                .and()
//                .logout();

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
