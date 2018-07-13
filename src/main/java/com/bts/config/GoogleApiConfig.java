package com.bts.config;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wagan8r on 7/7/18.
 */
@Configuration
public class GoogleApiConfig {
    @Bean
    HttpTransport getHttpTransport() {
        return new NetHttpTransport();
    }

    @Bean
    JsonFactory getJsonFactory() {
        return new JacksonFactory();
    }
}
