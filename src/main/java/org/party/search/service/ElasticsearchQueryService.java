package org.party.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.profile.ProfileShardResult;
import org.party.search.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;



@Service
@Log4j2
public class ElasticsearchQueryService {

    @Autowired
    private RestHighLevelClient client;


    public List<Article> query(String q) throws IOException {

        log.info("Search request {}", q);

        SearchRequest searchRequest = new SearchRequest("articles");

        SearchSourceBuilder searchSourceBuilder = getSearchSourceBuilder( q );

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


        RestStatus status = searchResponse.status();
        TimeValue took = searchResponse.getTook();
        Boolean terminatedEarly = searchResponse.isTerminatedEarly();
        boolean timedOut = searchResponse.isTimedOut();

        int totalShards = searchResponse.getTotalShards();
        int successfulShards = searchResponse.getSuccessfulShards();
        int failedShards = searchResponse.getFailedShards();
        for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
            // failures should be handled here
        }

        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();

        List<Article> articles = new ArrayList<>( searchHits.length );

        log.info("Response {}", searchResponse);

        for (SearchHit hit : searchHits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();

            log.info( hit.getSourceAsString() );

            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            ObjectMapper objectMapper = new ObjectMapper();

            Article article = objectMapper.readValue( hit.getSourceAsString(), Article.class);

            articles.add( article );
        }

        Map<String, ProfileShardResult> profilingResults = searchResponse.getProfileResults();
        for (Map.Entry<String, ProfileShardResult> profilingResult : profilingResults.entrySet()) {
            String key = profilingResult.getKey();
            ProfileShardResult profileShardResult = profilingResult.getValue();
        }

        return articles;

    }

    private SearchSourceBuilder getSearchSourceBuilder(String q) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.queryStringQuery( q ));

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);

        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        return searchSourceBuilder;
    }

    private QueryBuilder getQueryBuilder() {
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("user", "kimchy")
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(3)
                .maxExpansions(10);

        return queryBuilder;
    }

    private RestHighLevelClient getClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"), new HttpHost("localhost", 9201, "http")));

        return client;
    }
}

