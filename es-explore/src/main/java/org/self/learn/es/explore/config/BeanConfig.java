package org.self.learn.es.explore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 向spring容器注入应用共用的Bean
 *
 * @author ll
 * @since 2024-10-27 11:10
 */
@Configuration
public class BeanConfig {
    @Bean("defaultObjectMapper")
    public ObjectMapper defaultObjectMapper() {
        return new ObjectMapper();
    }
}
