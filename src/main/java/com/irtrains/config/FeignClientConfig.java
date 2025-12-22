package com.irtrains.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.*;
import jakarta.servlet.http.*;
import org.springframework.web.context.request.*;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // 1. Get the current request attributes (where the user's token lives)
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // 2. Extract the "Authorization" header (Bearer eyJhbG...)
                String authHeader = request.getHeader("Authorization");

                // 3. Forward it to the Payment Service
                if (authHeader != null) {
                    requestTemplate.header("Authorization", authHeader);
                }
            }
        };
    }
}
