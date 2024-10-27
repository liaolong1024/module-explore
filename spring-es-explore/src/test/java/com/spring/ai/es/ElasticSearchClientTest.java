package com.spring.ai.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ai.es.entity.TestIndex;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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

    @Autowired
    @Qualifier("defaultObjectMapper")
    private ObjectMapper objectMapper;

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
    void testAddAliasForIndex() throws IOException {
        IndicesAliasesRequest.AliasActions addAlias = IndicesAliasesRequest.AliasActions.add()
                .index("test_index_20241026113224").alias("test_index");
        IndicesAliasesRequest.AliasActions removeAlias = IndicesAliasesRequest.AliasActions.remove()
                .index("test_index_20241026113224").alias("test_index");

        IndicesAliasesRequest request = new IndicesAliasesRequest();
        request.addAliasAction(addAlias);
        request.addAliasAction(removeAlias);
        AcknowledgedResponse response = client.indices().updateAliases(request, RequestOptions.DEFAULT);
        LOGGER.info("alias action response: [{}]", response.isAcknowledged());
    }

    @Test
    void testDeleteIndex() throws IOException {
        String deleteIndex = "test_index_20241026101255";
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(deleteIndex);
        AcknowledgedResponse deleteResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        LOGGER.info("delete index [{}]: {}", deleteIndex, deleteResponse.isAcknowledged());
    }

    @Test
    void testAddDocument() throws IOException {
        TestIndex testIndex = TestIndex.builder().avgTicketPrice(BigDecimal.ONE).build();
        String json = objectMapper.writeValueAsString(testIndex);
        IndexRequest indexRequest = new IndexRequest().index("test_index_20241026113224")
                .source(json, XContentType.JSON);
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        LOGGER.info("add document result: [{}]", response.getResult());
    }

    @Test
    void testQueryDocument() throws IOException {
        GetRequest getRequest = new GetRequest()
                .index("test_index_20241026113224").id("H0QHzJIBuZgyxikAR9rM");
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        LOGGER.info("get response is [{}]", response.getSourceAsString());
    }

    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest().index("test_index_20241026113224")
                .id("H0QHzJIBuZgyxikAR9rM");
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        LOGGER.info("delete response is [{}]", response.getResult());
    }

    @Test
    void testSearchDocument() throws IOException {
        RangeQueryBuilder avgTicketPrice = new RangeQueryBuilder("AvgTicketPrice").gt(10);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(avgTicketPrice);
        SearchRequest searchRequest = new SearchRequest().indices("test_index_20241026113224")
                .source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        LOGGER.info("search response is [{}]", response.getHits().getTotalHits().value);
    }
}
