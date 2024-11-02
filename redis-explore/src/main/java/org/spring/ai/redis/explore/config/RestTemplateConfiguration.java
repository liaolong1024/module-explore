package org.spring.ai.redis.explore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author ll
 * @since 2024-10-22 23:15
 */
@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        return new RedisTemplate<>();
    }
}
