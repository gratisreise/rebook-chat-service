package com.example.rebookchatservice.client.trading;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "trading-service")
public interface TradingClient {
    // Trading service API methods will be added here
}
