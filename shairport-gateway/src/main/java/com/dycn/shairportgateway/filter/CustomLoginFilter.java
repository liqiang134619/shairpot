package com.dycn.shairportgateway.filter;

import com.alibaba.fastjson.JSON;
import com.dycn.shairportcommon.constant.ApiResponse;
import com.dycn.shairportcommon.constant.SecurityConstant;
import com.dycn.shairportcommon.constant.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;




/**
 * @author Liq
 * @date 2021-9-8
 */
@Slf4j
@Configuration
public class CustomLoginFilter implements GlobalFilter, Ordered {



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取当前请求url，若为包含 /login 或 /oauth/ 则不检查 access_token

        String requestUrl = exchange.getRequest().getURI().toString();

        if(requestUrl.contains("/api-docs")) {
            return chain.filter(exchange);
        }

        if (requestUrl.contains(SecurityConstant.OAUTH_URL) || requestUrl.contains(SecurityConstant.LOGIN_URL)) {
            return chain.filter(exchange);
        }

        ServerHttpResponse response = exchange.getResponse();

        // 从请求头信息获取 access_token 进行检查
        String accessToken = exchange.getRequest().getHeaders().getFirst(SecurityConstant.ACCESS_TOKEN);
        if (StringUtils.isBlank(accessToken)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            String jsonString = JSON.toJSONString(ApiResponse.ofStatus(Status.TOKEN_EXPIRED));
            return getVoidMono(response, jsonString);
        }


        // 判断 access_token 是否 Bearer  开头
        if (!accessToken.startsWith(SecurityConstant.BEARER_PREFIX)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            String jsonString = JSON.toJSONString(ApiResponse.ofStatus(Status.TOKEN_EXPIRED));
            return getVoidMono(response, jsonString);
        }


        // 检查access token的有效期
        log.info("进入 gateway 服务，执行 TokenFilter 过滤器，检查 Access Token 完成");
        return chain.filter(exchange);


    }

    private Mono<Void> getVoidMono(ServerHttpResponse response, String jsonString) {
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        byte[] datas = jsonString.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(datas);
        return response.writeWith(Mono.just(buffer));
    }


    @Override
    public int getOrder() {
         // 值越大，优先级越低
        return 10;
    }
}
