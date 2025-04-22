package com.movewave;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@ConfigurationPropertiesScan
@EnableCaching
@SpringBootApplication
public class MoveWaveApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MoveWaveApplication.class);
		app.run(args);
	}
}
