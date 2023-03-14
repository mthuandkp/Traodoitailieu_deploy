package com.sgu.userservice.config;


import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfiguration {
    @Value("${spring.cloud_name}")
    private String cloud_name;
    @Value("${spring.api_key}")
    private String api_key;
    @Value("${spring.api_secret}")
    private String api_secret;
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("cloud_name", cloud_name);
        valueMap.put("api_key", api_key);
        valueMap.put("api_secret", api_secret);
        return new Cloudinary(valueMap);
    }
}
