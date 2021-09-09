package com.dycn.shairporthealth.controller;

import com.dycn.shairportcommon.constant.ApiResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @author Liq
 * @date 2021/3/30
 */

@RestController
@Api(tags = "基础信息接口")
public class UserController {



    @GetMapping("/test")
    @ApiOperation("测试接口连通性")
    public ApiResponse tset() {
        return ApiResponse.ofSuccess();
    }

    @GetMapping(value = "get")
    @ApiOperation("获取用户token基础信息")
    @PreAuthorize("hasAnyRole('ROLE_TEST')")
    public Object get(Authentication authentication){
        authentication.getCredentials();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)authentication.getDetails();
        return details.getTokenValue();
    }


    @GetMapping(value = "get1")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("获取用户token基础信息1")
    public Object get1(Authentication authentication){
        authentication.getCredentials();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)authentication.getDetails();
        String jwtToken = details.getTokenValue();
        Claims claims = Jwts.parser()
                .setSigningKey("dev".getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(jwtToken)
                .getBody();
        return claims.get("jwt-ext");
//        return claims;
    }
}
