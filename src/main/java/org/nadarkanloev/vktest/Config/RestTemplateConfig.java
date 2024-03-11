package org.nadarkanloev.vktest.Config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Bean
    @RequestScope
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
