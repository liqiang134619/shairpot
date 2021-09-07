package com.dycn.shairportauth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Liq
 * @date 2021/3/31
 */

@Slf4j
@RestController
public class OAuth2Controller {


    @Autowired
    AuthenticationManager authenticationManager;



    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;


    @Autowired
    ClientDetailsService clientDetailsService;


    @PostMapping("/auth/login")
    public Object login(@RequestParam("username") String username,@RequestParam("password") String password) {


        // 申请令牌

        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic("user-client", "user-secret-8888");
        header.add("Authorization",httpBasic);
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);


        return null;

    }



    //获取httpbasic的串
    private String getHttpBasic(String clientId,String clientSecret){
        String string = clientId+":"+clientSecret;
        //将串进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic "+new String(encode);
    }











    @PostMapping("/auth/login2")
    public Object getUserTokenInfo(@RequestParam("username") String username,@RequestParam("password") String password) throws IOException {


        /**
         *   <1> 调用ClientDetailsService#loadClientByClientId 获取客户端信息封装到ClientDetails 对象
         *    clientDetails 用来管理客户端对象，
         *     * 实现1：InMemoryClientDetailsService 客户端信息在内存中
         *     * 实现2: JdbcClientDetialsService  客户端信息在数据库中
         *
         *
          */




        OAuth2AccessToken oauth2AccessToken = null;


        ClientDetails clientDetails = clientDetailsService.loadClientByClientId("user-client");

        TokenRequest tokenRequest = new TokenRequest(new HashMap<String,String>(), "user-client",clientDetails.getScope(),
                "password");

        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,  password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
//
        oauth2AccessToken =  authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        oAuth2Authentication.setAuthenticated(true);

        return oauth2AccessToken;







//         <1> 创建 ResourceOwnerPasswordResourceDetails 对象
//        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
//        resourceDetails.setAccessTokenUri("http://localhost:6001/oauth/token");
//        resourceDetails.setClientId("user-client");
//        resourceDetails.setClientSecret("user-secret-8888");
//        resourceDetails.setUsername(username);
//        resourceDetails.setPassword(password);
//        // <2> 创建 OAuth2RestTemplate 对象
//        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
//        restTemplate.setAccessTokenProvider(new ResourceOwnerPasswordAccessTokenProvider());
//        // <3> 获取访问令牌  调用授权服务器的 /oauth/token 接口
//        DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken)restTemplate.getAccessToken();
//
//        // 存储到redis
//
//        return accessToken.getValue();

    }
}
