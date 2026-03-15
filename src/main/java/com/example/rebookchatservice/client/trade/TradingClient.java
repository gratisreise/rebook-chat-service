package com.example.rebookchatservice.client.trade;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "trade-service")
public interface TradingClient {
    // Trading service API methods will be added here
}
