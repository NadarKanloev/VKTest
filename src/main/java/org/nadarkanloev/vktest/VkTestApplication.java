package org.nadarkanloev.vktest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VkTestApplication {
    public static void main(String[] args){
        SpringApplication.run(VkTestApplication.class, args);
    }

}
