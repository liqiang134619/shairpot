package com.dycn.shairportgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @author Liq
 * @date 2021/3/29
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ShairportGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShairportGatewayApplication.class, args);
	}





}
