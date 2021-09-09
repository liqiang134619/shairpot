package com.dycn.shairportgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author Liq
 * @date 2021-9-8
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ShairportGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShairportGatewayApplication.class, args);
	}





}
