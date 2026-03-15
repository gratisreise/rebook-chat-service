package com.example.rebookchatservice.global.passport;

import com.rebook.passport.HmacUtil;
import com.rebook.passport.PassportDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PassportConfig {
    @Bean
    HmacUtil hmacUtil(
        @Value("${passport.secret-key}") String secretKey,
        @Value("${passport.hmac-algorithm}") String hmacAlgorithm
    ) {
        return new HmacUtil(secretKey, hmacAlgorithm);
    }

    @Bean
    public PassportDecoder passportDecoder(HmacUtil hmacUtil) {
        return new PassportDecoder(hmacUtil);
    }
}
