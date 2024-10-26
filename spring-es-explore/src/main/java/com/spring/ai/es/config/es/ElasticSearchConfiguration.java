package com.spring.ai.es.config.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ElasticSearch配置
 *
 * @author ll
 * @since 2024-10-26 15:48
 */
@Configuration
public class ElasticSearchConfiguration {
    @Bean
    public RestHighLevelClient restHighLevelClient(ElasticSearchConfigBean bean) {
        HttpHost[] httpHosts = new HttpHost[bean.getHosts().size()];
        for (int i = 0; i < bean.getHosts().size(); i++) {
            httpHosts[i] = HttpHost.create(bean.getHosts().get(i));
        }
        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);
        return new RestHighLevelClient(restClientBuilder);
    }
}
