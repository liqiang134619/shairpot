package com.dycn.shairportauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 *
 *  * @author Liq
 *
 *
 *
 * */
@SpringBootApplication
@EnableDiscoveryClient
public class ShairportAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShairportAuthApplication.class, args);
	}

}
