package com.spring.ai.es.config.es;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author ll
 * @since 2024-10-26 16:01
 */
@ConfigurationProperties(prefix = "es.conf")
@Configuration
@Setter
@Getter
public class ElasticSearchConfigBean {
    private List<String> hosts;
}
