package com.visualpathit.account.utils;

import java.io.IOException;

import jakarta.annotation.PreDestroy;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visualpathit.account.beans.Components;

@Service
public class ElasticsearchUtil {
	
	private static Components object;
	private static volatile ElasticsearchClient elasticsearchClient;
	private static RestClient restClient;

    @Autowired
    public void setComponents(Components object) {
    	ElasticsearchUtil.object = object;    	
    	
    }
    public static ElasticsearchClient elasticsearchClient() {
        if (elasticsearchClient == null) {
            synchronized (ElasticsearchUtil.class) {
                if (elasticsearchClient == null) {
                    String elasticsearchHost = object.getElasticsearchHost();
                    String elasticsearchPort = object.getElasticsearchPort();
                    restClient = RestClient.builder(new HttpHost(
                            elasticsearchHost, Integer.parseInt(elasticsearchPort), "http")).build();
                    elasticsearchClient = new ElasticsearchClient(
                            new RestClientTransport(restClient, new JacksonJsonpMapper()));
                }
            }
        }
        return elasticsearchClient;
      }

	@PreDestroy
	public void closeElasticsearchClient() throws IOException {
		synchronized (ElasticsearchUtil.class) {
			if (restClient != null) {
				restClient.close();
				restClient = null;
				elasticsearchClient = null;
			}
		}
	}
}
