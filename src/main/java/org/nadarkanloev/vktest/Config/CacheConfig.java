package org.nadarkanloev.vktest.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    private Long cacheTimeToLive = 1000000L ;
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisSerializationContext.SerializationPair<Object> jsonSerializer =
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());

        return RedisCacheManager.builder(redisConnectionFactory)
                .withCacheConfiguration("postsCache", RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(jsonSerializer)
                        .entryTtl(Duration.ofSeconds(cacheTimeToLive)))
                .withCacheConfiguration("userCache", RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(jsonSerializer)
                        .entryTtl(Duration.ofSeconds(cacheTimeToLive)))
                .withCacheConfiguration("albumCache", RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(jsonSerializer)
                        .entryTtl(Duration.ofSeconds(cacheTimeToLive)))
                .build();
    }
}
