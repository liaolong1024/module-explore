package org.self.learn.es.explore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.fetch.subphase.FieldAndFormat;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.self.learn.es.explore.entity.TestIndex;
import org.apache.commons.io.IOUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
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
import java.util.Arrays;
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
        LOGGER.info("add document result: [{}]", response.getId());
    }

    @Test
    void testSyncBulkAddDocument() throws IOException {
        BulkRequest request = new BulkRequest();
        for (int i = 0; i < 5; i++) {
            TestIndex testIndex = TestIndex.builder().avgTicketPrice(BigDecimal.valueOf(i)).build();
            String json = objectMapper.writeValueAsString(testIndex);
            request.add(new IndexRequest().index("test_index_20241026113224").source(json, XContentType.JSON));
        }
        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
        LOGGER.info("sync bulk add doc response: [{}]", bulkResponse.status());
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
        String index = "test_index_20241026113224";

        // range search
        RangeQueryBuilder avgTicketPrice = new RangeQueryBuilder("AvgTicketPrice").gt(10);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(avgTicketPrice);
        SearchRequest searchRequest = new SearchRequest().indices(index)
                .source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        LOGGER.info("range search response is [{}]", response.getHits().getTotalHits().value);

        // match query
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("OriginCountry", "DE");
        SearchSourceBuilder mathQuerySourceBuilder = new SearchSourceBuilder().query(matchQueryBuilder);
        SearchRequest matchQuerySearchRequest = new SearchRequest().indices(index)
                .source(mathQuerySourceBuilder);
        SearchResponse matchResponse = client.search(matchQuerySearchRequest, RequestOptions.DEFAULT);
        LOGGER.info("match search response first doc id is [{}]",
                matchResponse.getHits().getAt(0).getId());

        // term query & field_sort
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("currency", "EUR");
        FieldSortBuilder customerBirthDate = new FieldSortBuilder("customer_birth_date")
                .order(SortOrder.ASC);
        SearchSourceBuilder sortSourceBuilder = new SearchSourceBuilder().query(termQueryBuilder)
                .sort(customerBirthDate).sort("customer_first_name.keyword", SortOrder.DESC);
        LOGGER.info("search request DSL is: {}", sortSourceBuilder);
        SearchRequest sortRequest = new SearchRequest().indices("kibana_sample_data_ecommerce")
                .source(sortSourceBuilder);
        SearchResponse sortResponse = client.search(sortRequest, RequestOptions.DEFAULT);
        LOGGER.info("sort response is [{}]", sortResponse.getHits().getAt(0));

        // term query & geo sort
        TermQueryBuilder termQueryForGeo = new TermQueryBuilder("currency", "EUR");
        GeoDistanceSortBuilder geoDistanceSortBuilder = new GeoDistanceSortBuilder("geoip.location", -70, 40)
                .geoDistance(GeoDistance.ARC)
                .sortMode(SortMode.MIN)
                .order(SortOrder.ASC)
                .unit(DistanceUnit.KILOMETERS)
                .ignoreUnmapped(true);
        SearchSourceBuilder geoSortSourceBuilder = new SearchSourceBuilder().query(termQueryForGeo)
                .sort(geoDistanceSortBuilder);
        LOGGER.info("geoSortSourceBuilder DSL: {}", geoSortSourceBuilder);
        SearchRequest geoSearchRequest = new SearchRequest().indices("kibana_sample_data_ecommerce").source(geoSortSourceBuilder);
        SearchResponse geoSortResponse = client.search(geoSearchRequest, RequestOptions.DEFAULT);
        LOGGER.info("geo sort query response: {}", geoSortResponse.getHits().getAt(0));
    }

    /**
     * 分页查找
     */
    @Test
    void testPaginationSearchDocument() throws IOException {
        SearchSourceBuilder paginationSourceBuilder = new SearchSourceBuilder()
                .from(5).size(5).query(new MatchQueryBuilder("currency", "EUR"));
        SearchRequest paginationSearchRequest = new SearchRequest()
                .indices("kibana_sample_data_ecommerce")
                .source(paginationSourceBuilder);
        SearchResponse paginationResponse = client.search(paginationSearchRequest, RequestOptions.DEFAULT);
        LOGGER.info("pagination response hit size is {}", paginationResponse.getHits().getTotalHits());

        // 使用search_after连续分页查询时，获取PIT以并在后续查询中指定pit.id可保证查询数据一致性
        Request pitRequest = new Request("POST", "/kibana_sample_data_ecommerce/_pit?keep_alive=1m");
        Response response = client.getLowLevelClient().performRequest(pitRequest);
        String pitId = objectMapper.readTree(EntityUtils.toString(response.getEntity())).get("id").asText();
        LOGGER.info("pit is {}", pitId);
        // do something...
        Request deletePitRequest = new Request("DELETE", "/_pit");
        ObjectNode objectNode = objectMapper.createObjectNode().put("id", pitId);
        deletePitRequest.setJsonEntity(objectNode.toString());
        Response deletePitResponse = client.getLowLevelClient().performRequest(deletePitRequest);
        LOGGER.info("delete pit response is {}", EntityUtils.toString(deletePitResponse.getEntity()));
    }

    @Test
    void testSelectedFieldsSearch() throws IOException {
        String index = "kibana_sample_data_ecommerce";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .fetchField("currency")
                .fetchField(new FieldAndFormat("order_date", YYYYMMDDHHMMSS))
                .fetchSource(false);
        LOGGER.info("selected field source builder: {}", searchSourceBuilder);
        SearchRequest searchRequest = new SearchRequest().indices(index)
                .source(searchSourceBuilder);
        SearchResponse selectedFieldResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);

        LOGGER.info("{}", selectedFieldResponse.getHits().getAt(0));
    }

    @Test
    void testCollapseSearch() throws IOException {
        String index = "kibana_sample_data_ecommerce";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(new MatchQueryBuilder("currency", "EUR"))
                .collapse(new CollapseBuilder("customer_gender"))
                .from(0);
        LOGGER.info("selected field source builder: {}", searchSourceBuilder);
        SearchRequest searchRequest = new SearchRequest().indices(index)
                .source(searchSourceBuilder);
        SearchResponse selectedFieldResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);

        LOGGER.info("{}", selectedFieldResponse.getHits());
    }

    @Test
    void testFilterSearchResult() throws IOException {
        String index = "kibana_sample_data_ecommerce";

        // just bool query filter
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .filter(new TermQueryBuilder("currency", "EUR"))
                .filter(new TermQueryBuilder("customer_gender", "MALE"));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(boolQueryBuilder);
        LOGGER.info("selected field source builder: {}", searchSourceBuilder);
        SearchRequest searchRequest = new SearchRequest().indices(index)
                .source(searchSourceBuilder);
        SearchResponse selectedFieldResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);
        LOGGER.info("{}", selectedFieldResponse.getHits().getTotalHits());

        // bool query filter & post_filter
        BoolQueryBuilder boolQueryBuilder2 = new BoolQueryBuilder()
                .filter(new TermQueryBuilder("currency", "EUR"));
        SearchSourceBuilder searchSourceBuilder2 = new SearchSourceBuilder()
                .query(boolQueryBuilder2)
                .postFilter(new TermQueryBuilder("customer_gender", "MALE"));
        LOGGER.info("selected field source builder2: {}", searchSourceBuilder2);
        SearchRequest searchRequest2 = new SearchRequest().indices(index)
                .source(searchSourceBuilder2);
        SearchResponse selectedFieldResponse2 =
                client.search(searchRequest2, RequestOptions.DEFAULT);
        LOGGER.info("{}", selectedFieldResponse2.getHits().getTotalHits());

    }

    @Test
    void testHighlightQuery() throws IOException {
        String index = "kibana_sample_data_ecommerce";
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(new MatchQueryBuilder("currency", "EUR"))
                .highlighter(new HighlightBuilder().field("currency").highlighterType("unified"));
        LOGGER.info("{}", sourceBuilder);
        SearchRequest searchRequest = new SearchRequest().indices(index).source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        LOGGER.info("{}", Arrays.toString(searchResponse.getHits().getHits()));
    }
}
