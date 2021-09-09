package com.dycn.shairporthealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
public class ShairportHealthApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShairportHealthApplication.class, args);
	}

}
