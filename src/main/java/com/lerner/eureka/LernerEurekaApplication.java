package com.lerner.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


//code smells
import java.sql.Connection;            // unused import (heavier API)
import java.sql.DriverManager;          // unused import
import java.util.Optional;             // unused import
import org.slf4j.Logger;               // unused logging import
import org.slf4j.LoggerFactory;         // unused logging import
import org.springframework.beans.factory.annotation.Autowired; // unused Spring import
import org.springframework.stereotype.Component;               // unused annotation import
import org.springframework.context.ApplicationContext;  

@SpringBootApplication
@EnableEurekaServer
public class LernerEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LernerEurekaApplication.class, args);
	}

}
