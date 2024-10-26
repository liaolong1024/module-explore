package com.spring.ai.es;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ElasticSearchClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchClientTest.class);

    private static final String TEST_INDEX = "test_index";

    private static final String YYYYMMDDHHMMSS = "yyyyMMddhhmmss";

    private String createNewIndex;

    private String flights_mapping_setting_alias;

    @Autowired
    private RestHighLevelClient client;

    @Test
    void testLogBack() {
        LOGGER.info("abc");
    }

    @BeforeEach
    public void initBeforeCreateIndex() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);
        LocalDateTime now = LocalDateTime.now();
        createNewIndex = TEST_INDEX + "_" + dateTimeFormatter.format(now);

        String flights_mapping_setting_alias_path = "flights_mapping_setting_alias.json";
        InputStream resourceAsStream =
                this.getClass().getClassLoader().getResourceAsStream(flights_mapping_setting_alias_path);
        if (resourceAsStream == null) {
            throw new IOException(flights_mapping_setting_alias_path + " not found");
        }
        flights_mapping_setting_alias = IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8.name());
    }

    /**
     * 索引创建
     *
     * @throws IOException io
     */
    @Test
    void testCreateIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(createNewIndex);
        createIndexRequest.source(flights_mapping_setting_alias, XContentType.JSON);
        CreateIndexResponse response = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        LOGGER.info("create index [{}]: {}", createNewIndex, response.isAcknowledged());
    }

    @Test
    void testGetIndex() throws IOException {
        String existIndex = "test_index_20241026113224";
        GetIndexRequest getIndexRequest = new GetIndexRequest(existIndex);
        GetIndexResponse getIndexResponse = client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
        Map<String, List<AliasMetadata>> aliases = getIndexResponse.getAliases();
        Map<String, MappingMetadata> mappings = getIndexResponse.getMappings();
        Map<String, Settings> settings = getIndexResponse.getSettings();
        LOGGER.info("[alias]: {}", aliases.get(existIndex));
        LOGGER.info("[mappings]: {}", mappings.get(existIndex).getSourceAsMap());
        LOGGER.info("[settings]: {}", settings.get(existIndex));
    }

    /**
     * 修改索引Mapping
     *
     * @throws IOException io
     */
    @Test
    void testModifyIndexMapping() throws IOException {
        String existIndex = "test_index_20241026113224";
        PutMappingRequest putMappingRequest = new PutMappingRequest(existIndex)
                .source("{\"properties\":{\"test_add_new_field\":{\"type\":\"keyword\"}}}",
                        XContentType.JSON);
        AcknowledgedResponse acknowledgedResponse =
                client.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
        LOGGER.info("modify mapping result: [{}]", acknowledgedResponse.isAcknowledged());
    }

    @Test
    void testDeleteIndex() throws IOException {
        String deleteIndex = "test_index_20241026101255";
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(deleteIndex);
        AcknowledgedResponse deleteResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        LOGGER.info("delete index [{}]: {}", deleteIndex, deleteResponse.isAcknowledged());
    }
}
