package com.lerner.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors; // unused
import org.springframework.web.client.RestTemplate; // unused

@SpringBootApplication
@EnableEurekaServer
public class LernerEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LernerEurekaApplication.class, args);
	}

}
