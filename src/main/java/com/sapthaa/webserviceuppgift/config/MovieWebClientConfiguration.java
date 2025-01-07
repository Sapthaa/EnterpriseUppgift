package com.sapthaa.webserviceuppgift.config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MovieWebClientConfiguration {

    @Bean
    public WebClient.Builder movieWebClientBuilder() {
        return WebClient.builder();
    }
}
