package com.movewave;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@Slf4j
@ConfigurationPropertiesScan
@EnableCaching
@EnableRetry
@SpringBootApplication
@EnableFeignClients
public class MoveWaveApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MoveWaveApplication.class);
		app.run(args);
	}
}
