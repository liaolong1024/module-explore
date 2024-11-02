package org.self.learn.es.explore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SpringEsExploreApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringEsExploreApplication.class, args);
    }

}
