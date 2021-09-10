package com.dycn.shairportauth.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Liq
 * @date 2021/3/30
 */

public class JWTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();



        // 获取userDetailsService获取的userDetail对象
        Object principal = oAuth2Authentication.getPrincipal();

        // 强转为自定义userDetails
        // 此处可以添加一些用户的基础信息



        additionalInfo.put("jwt-ext", "JWT 扩展信息");
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
        return oAuth2AccessToken;
    }
}
