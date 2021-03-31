package com.dycn.shairportauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * @author Liq
 * @date 2021/3/30
 */
@Configuration
public class JwtTokenConfig {

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("dev");
//        accessTokenConverter.setKeyPair(keyPair());
        return accessTokenConverter;
    }

    @Bean
    public TokenEnhancer jwtTokenEnhancer(){
        return new JWTokenEnhancer();
    }

    /**
     * 其中对JWT签名有对称和非对称两种方式：
     *
     * 对称方式：认证服务器和资源服务器使用同一个密钥进行加签和验签 ，默认算法HMAC
     *
     * 非对称方式：认证服务器使用私钥加签，资源服务器使用公钥验签，默认算法RSA
     *
     * 非对称方式相较于对称方式更为安全，因为私钥只有认证服务器知道。
     *
     * 项目中使用RSA非对称签名方式，具体实现步骤如下：
     *
     * (1). 从密钥库获取密钥对(密钥+私钥)
     * (2). 认证服务器私钥对token签名
     * (3). 提供公钥获取接口供资源服务器验签使用
     */

    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
//    @Bean
//    public KeyPair keyPair() {
//        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(
//                new ClassPathResource("youlai.jks"), "123456".toCharArray());
//        KeyPair keyPair = factory.getKeyPair(
//                "youlai", "123456".toCharArray());
//        return keyPair;
//    }
}
