package com.dycn.shairportauth.config;

import com.dycn.shairportauth.service.CustomUserDetailsServiceImpl;
import com.dycn.shairportcommon.constant.Status;
import com.dycn.shairportcommon.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Liq
 * @date 2021/3/30
 */
@Configuration
@EnableAuthorizationServer
/**
 * 放在资源服务器中扫描，放在根目录扫描出错。不知道为啥 cnm
 */
@ComponentScan(basePackages = {"com.dycn.shairportcommon"})
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    CustomUserDetailsServiceImpl customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;





    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // client 信息直接放在内存中校验，有需要添加数据库认证
        clients.inMemory()
                .withClient("health-client")
                .secret(passwordEncoder.encode("health-secret-8888"))
                // 拥有的认证模式
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .accessTokenValiditySeconds(3600)
                .scopes("all");

    }




    @Autowired
    private WebResponseExceptionTranslator webResponseExceptionTranslator;


    @Autowired
    private TokenStore jwtTokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private TokenEnhancer jwtTokenEnhancer;


    /**
     * token 生成细节描述
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        //配置token的存储方法
        defaultTokenServices.setAccessTokenValiditySeconds(60 * 60 * 12);
        defaultTokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenStore(jwtTokenStore);


        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancerList = new ArrayList<>();
        enhancerList.add(jwtTokenEnhancer);
        enhancerList.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancerList);
        defaultTokenServices.setTokenEnhancer(enhancerChain);


        return defaultTokenServices;
    }




    /**
     * 配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        /**
         * jwt 增强模式
         */

//        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
//        List<TokenEnhancer> enhancerList = new ArrayList<>();
//        enhancerList.add(jwtTokenEnhancer);
//        enhancerList.add(jwtAccessTokenConverter);
//        enhancerChain.setTokenEnhancers(enhancerList);
        endpoints.tokenServices(tokenServices());
        endpoints.tokenStore(jwtTokenStore)
                //  password 模式
                .userDetailsService(customUserDetailsService)
                .authenticationManager(authenticationManager)
//                .tokenEnhancer(enhancerChain)
                // refresh token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                //      1 重复使用：access token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                //      2 非重复使用：access token过期刷新时， refresh token过期时间延续，在refresh token有效期内刷新便永不失效达到无需再次登录的目的
                .reuseRefreshTokens(true);
//        endpoints.exceptionTranslator(webResponseExceptionTranslator);


    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 客户端访问的权限
        security.allowFormAuthenticationForClients()
                .passwordEncoder(new BCryptPasswordEncoder())
                .checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("isAuthenticated()");
    }


}















