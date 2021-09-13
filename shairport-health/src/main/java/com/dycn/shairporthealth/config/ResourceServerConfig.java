package com.dycn.shairporthealth.config;

import com.dycn.shairportcommon.constant.Status;
import com.dycn.shairportcommon.util.ResponseUtil;
import com.dycn.shairporthealth.exception.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;


/**
 * @author Liq
 * @date 2021-9-8
 */
@Configuration
@EnableResourceServer
@ComponentScan(basePackages = {"com.dycn.shairportcommon"})
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    @Autowired
    private OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;


    @Autowired
    private OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler;


    /****************************
     * 以上是 RedisToken 方式的配置
     * --------------------------
     * 以下是 JWT 方式配置
     ****************************/


    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();

        accessTokenConverter.setSigningKey("dev");
        accessTokenConverter.setVerifierKey("dev");
        return accessTokenConverter;
    }

    @Autowired
    private TokenStore jwtTokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(jwtTokenStore);
        //这个貌似是配置要不要把token信息记录在session中
        resources.stateless(true);
        resources.accessDeniedHandler(oAuth2AccessDeniedHandler);
        resources.authenticationEntryPoint(authenticationEntryPoint);
        resources.expressionHandler(oAuth2WebSecurityExpressionHandler);


    }


    //Http安全配置，对每个到达系统的http请求链接进行校验
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.headers().frameOptions().disable();

        //所有请求必须认证通过
        http.authorizeRequests()
                //下边的路径放行
                .antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui",
                        "/swagger-resources", "/swagger-resources/configuration/security",
                        "/swagger-ui.html", "/webjars/**", "/health/v2/api-docs","/test","/**").permitAll()
                .anyRequest().authenticated();


        //这个貌似是配置要不要把token信息记录在session中
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }


    //    @Value("${security.oauth2.client.client-id}")
//    private String clientId;
//
//    @Value("${security.oauth2.client.client-secret}")
//    private String secret;
//
//    @Value("${security.oauth2.authorization.check-token-access}")
//    private String checkTokenEndpointUrl;

//    @Autowired
//    private RedisConnectionFactory redisConnectionFactory;
//
//    @Bean
//    public TokenStore redisTokenStore (){
//        return new RedisTokenStore(redisConnectionFactory);
//    }
//
//    @Bean
//    public RemoteTokenServices tokenService() {
//        RemoteTokenServices tokenService = new RemoteTokenServices();
//        tokenService.setClientId(clientId);
//        tokenService.setClientSecret(secret);
//        tokenService.setCheckTokenEndpointUrl(checkTokenEndpointUrl);
//        return tokenService;
//    }
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.tokenServices(tokenService());
//    }


}
