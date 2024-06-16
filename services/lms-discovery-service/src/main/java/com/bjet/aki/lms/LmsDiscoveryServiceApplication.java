package com.bjet.aki.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LmsDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsDiscoveryServiceApplication.class, args);
	}

}
