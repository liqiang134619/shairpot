package com.dycn.shairportmqconsumer.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Liq
 * @date 2021/3/29
 */

@FeignClient("alibaba-nacos-discovery-server")
public interface MainServiceClient {

    @GetMapping("/hello")
    String hello(@RequestParam(name = "name") String name);
}
