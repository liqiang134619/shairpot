package com.dycn.shairportauth.config;

import com.dycn.shairportauth.service.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Liq
 * @date 2021/3/30
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {



    @Autowired
    private TokenEnhancer jwtTokenEnhancer;

    @Bean
    public TokenEnhancer jwtTokenEnhancer(){
        return new JWTokenEnhancer();
    }


    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    CustomUserDetailsServiceImpl customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore jwtTokenStore;

    @Autowired(required = false)
    private JWTokenEnhancer jWTokenEnhancer;



    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 存放redis token
        // 存放jwt token
        // 验证方式
//        endpoints.authenticationManager(authenticationManager)
//                // 验证规则
//                .userDetailsService(customUserDetailsService)
//                .accessTokenConverter(jwtAccessTokenConverter)
//
//                // 存放位置
//                .tokenStore(jwtTokenStore);





        if (jwtAccessTokenConverter != null) {
            if (jWTokenEnhancer != null) {
                TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
                tokenEnhancerChain.setTokenEnhancers(
                        Arrays.asList(jwtTokenEnhancer, jwtAccessTokenConverter));
                endpoints.tokenEnhancer(tokenEnhancerChain);
            } else {
                endpoints.accessTokenConverter(jwtAccessTokenConverter);
            }
        }
        endpoints.tokenStore(jwtTokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(customUserDetailsService);
//                .authorizationCodeServices(authorizationCodeServices)
//                .exceptionTranslator(webResponseExceptionTranslator);


















        /**z`
         * jwt 增强模式
         */
//        TokenEnhancerChain enhancerChain	= new TokenEnhancerChain();
//        List<TokenEnhancer> enhancerList	= new ArrayList<>();
//        enhancerList.add( jwtTokenEnhancer );
//        enhancerList.add( jwtAccessTokenConverter );
//        enhancerChain.setTokenEnhancers( enhancerList );
//        endpoints.tokenStore( jwtTokenStore )
//                .userDetailsService( customUserDetailsService )
//                /**
//                 * 支持 password 模式
//                 */
//                .authenticationManager( authenticationManager )
//                .tokenEnhancer( enhancerChain )
//                .accessTokenConverter( jwtAccessTokenConverter );
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        // 配置存在的客户，内存写死
        /**
         * authorization_code：授权码类型。
         * implicit：隐式授权类型。
         * password：资源所有者（即用户）密码类型。
         * client_credentials：客户端凭据（客户端ID以及Key）类型。
         * refresh_token：通过以上授权获得的刷新令牌来获取新的令牌。
         */
        clients.inMemory()
                .withClient("user-client")
                .secret(passwordEncoder.encode("user-secret-8888"))
                // 拥有的认证模式
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .accessTokenValiditySeconds(3600)
                .scopes("all");


        //数据库认证

//        JdbcClientDetailsServiceBuilder jcsb = clients.jdbc(dataSource);
//        jcsb.passwordEncoder(passwordEncoder);

    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 客户端访问的权限
        security.allowFormAuthenticationForClients()
                .checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("isAuthenticated()");
    }
}















