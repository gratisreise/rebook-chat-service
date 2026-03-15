package com.example.rebookchatservice.global;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableFeignClients
@EnableScheduling
public class AppConfig {
}
