package com.hll.gmall.search.conf;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * ElasticSearch 配置
 */
@Configuration
public class ElasticSearchConfig {

    @Value("${spring.elasticsearch.rest.uris}")
    private String uris;

    @Value("${spring.elasticsearch.rest.connection-timeout}")
    private int connectionTimeout;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        // 拆分地址
        List<HttpHost> hostLists = new ArrayList<>();
        String[] hostList = uris.split(",");
        for (String uri : hostList) {
            String host = uri.split(":")[0];
            String port = uri.split(":")[1];
            hostLists.add(new HttpHost(host, Integer.parseInt(port)));
        }
        // 转换成 HttpHost 数组
        HttpHost[] httpHost = hostLists.toArray(new HttpHost[]{});
        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHost);
        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectionTimeout);
            return requestConfigBuilder;
        });
        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder);
        return new RestHighLevelClient(builder);
    }

}
