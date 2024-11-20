package com.banquemisr.challenge05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.banquemisr.challenge05")
@EnableAsync
@EnableCaching
@EntityScan(basePackages = "com.banquemisr.challenge05.model")
@EnableJpaRepositories(basePackages = "com.banquemisr.challenge05.repository")
public class TaskManagementSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(TaskManagementSystemApplication.class, args);
	}
}
