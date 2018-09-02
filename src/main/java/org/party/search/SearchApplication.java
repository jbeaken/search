package org.party.search;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.party.search.service.ElasticsearchIndexService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan(basePackages =  "org.party.search.domain" )
public class SearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
	}

	@Bean
	ApplicationRunner init(ElasticsearchIndexService elasticsearchService) {
		return args -> {  elasticsearchService.bulkIndex();	};
	}

	@Bean
	RestHighLevelClient restHighLevelClient() {
		return new RestHighLevelClient(
				RestClient
						.builder(new HttpHost("localhost", 9200))
						.setRequestConfigCallback(config -> config
								.setConnectTimeout(5_000)
								.setConnectionRequestTimeout(5_000)
								.setSocketTimeout(5_000)
						)
						.setMaxRetryTimeoutMillis(5_000));

	}

}
