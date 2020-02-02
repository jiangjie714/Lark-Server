package com.github.hollykunge.security.cron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *
 */
@SpringBootApplication
@EnableEurekaClient
public class CronApplication {

	public static void main(String[] args) {
        SpringApplication.run(CronApplication.class, args);
	}

}