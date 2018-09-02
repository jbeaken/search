package org.party.search.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;

import lombok.extern.log4j.Log4j2;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.party.search.domain.Article;
import org.party.search.repository.ArticleRepository;

@Service
@Log4j2
public class ElasticsearchIndexService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private RestHighLevelClient client;

    public void bulkIndex() throws IOException {

        int count = 0;

        Collection<Article> articles = articleRepository.findAll( PageRequest.of(count, 1000));

        while( !articles.isEmpty() ) {

            count++;

            BulkRequest request = new BulkRequest();

            articles.forEach(s -> {
                IndexRequest r = getIndexRequest(s);
                request.add(r);
            });

            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);

            log.info("Indexed {} items in {} nanos", bulkResponse.getItems().length, bulkResponse.getIngestTook().getNanos());

            articles = articleRepository.findAll( PageRequest.of(count, 1000) );
        }
    }


    private IndexRequest getIndexRequest(Article article) {
        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder();
            LocalDateTime published = article.getPublished() == null ? null : article.getPublished().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            builder.startObject();
            {
                builder.field("headline", article.getHeadline());
                builder.timeField("published", published );
                builder.timeField("summary", article.getSummary());
            }
            builder.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IndexRequest request = new IndexRequest("articles", "article").id( article.getId().toString() ).source(builder);
        return request;
    }

//    private RestHighLevelClient getClient() {
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(new HttpHost("localhost", 9200, "http"), new HttpHost("localhost", 9201, "http")));
//
//        return client;
//    }

}

