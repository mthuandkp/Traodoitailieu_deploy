package com.sgu.apigateway.config;

import com.sgu.apigateway.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("http://AUTH-SERVICE:8081"))
                .route("user-service", r -> r.path("/api/v1/account/**","/api/v1/person/**","/api/v1/user/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://USER-SERVICE:8082"))
                .route("posts-service", r -> r.path("/api/v1/category/**","/api/v1/posts/**","/api/v1/posts-image/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://POSTS-SERVICE:8083"))
                .build();
    }

}
