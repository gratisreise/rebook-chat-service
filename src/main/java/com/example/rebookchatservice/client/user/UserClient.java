package com.example.rebookchatservice.client.user;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service")
public interface UserClient {
    // User service API methods will be added here
}
