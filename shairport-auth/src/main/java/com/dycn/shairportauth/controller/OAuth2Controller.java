package com.dycn.shairportauth.controller;

import com.dycn.shairportcommon.exception.CommonException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author Liq
 * @date 2021/3/31
 */

@Slf4j
@RestController
@Api(tags = "认证接口")
@RequestMapping("/auth")
public class OAuth2Controller {


    @Autowired
    AuthenticationManager authenticationManager;


    @Qualifier("tokenServices")
    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    ClientDetailsService clientDetailsService;




    @GetMapping("/test")
    @ApiOperation(value = "测试连通性",response = String.class)
    public String test1() {

        return "测试连通性";

    }



    @ApiOperation("系统账号密码登陆")
    @PostMapping("/login")
    public Object getUserTokenInfo(@ApiParam("用户名") @RequestParam("username") String username, @ApiParam("密码") @RequestParam("password") String password)  {

        log.info("【==>  开始登陆】");
        try {
            OAuth2AccessToken oauth2AccessToken = null;

            ClientDetails clientDetails = clientDetailsService.loadClientByClientId("health-client");
            TokenRequest tokenRequest = new TokenRequest(new HashMap<String, String>(1), "health-client", clientDetails.getScope(),
                    "password");
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            oauth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
            oAuth2Authentication.setAuthenticated(true);
            return oauth2AccessToken;
        } catch (Exception e) {
            if(e instanceof BadCredentialsException) {
                throw new CommonException("用户名或密码错误");
            } else {
                throw new CommonException(e.getMessage());
            }
        }


































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


/*
    @PostMapping("/auth/login")
    public Object login(@RequestParam("username") String username, @RequestParam("password") String password) {


        // 申请令牌

        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic("user-client", "user-secret-8888");
        header.add("Authorization", httpBasic);
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);


        return null;

    }


    //获取httpbasic的串
    private String getHttpBasic(String clientId, String clientSecret) {
        String string = clientId + ":" + clientSecret;
        //将串进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic " + new String(encode);
    }

    */
}
