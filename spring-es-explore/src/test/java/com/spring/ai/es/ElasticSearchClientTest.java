package com.spring.ai.es;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ElasticSearchClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchClientTest.class);

    private static final String TEST_INDEX = "test_index";

    @Autowired
    private RestHighLevelClient client;

    @Test
    void contextLoads() {
    }

    @Test
    void testLogBack() {
        Logger LOGGER = LoggerFactory.getLogger(ElasticSearchClientTest.class);
        System.out.println(LOGGER.getName());
        LOGGER.info("abc");
    }

    @Test
    void testCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(TEST_INDEX);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        LOGGER.info("create index [{}]: {}", TEST_INDEX, response.isAcknowledged());
    }
}
