package com.visualpathit.account.utils;

import java.net.InetSocketAddress;

import jakarta.annotation.PreDestroy;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visualpathit.account.beans.Components;
@Service
public class ElasticsearchUtil {
	
	private static Components object;
	private static volatile TransportClient transportClient;

    @Autowired
    public void setComponents(Components object){
    	ElasticsearchUtil.object = object;    	
    	
    }
    @SuppressWarnings("resource")
    public static TransportClient trannsportClient() {
    	if (transportClient == null) {
    		synchronized (ElasticsearchUtil.class) {
    			if (transportClient == null) {
    		    	String elasticsearchHost =object.getElasticsearchHost();
    		    	String elasticsearchPort =object.getElasticsearchPort();
    		    	String elasticsearchCluster =object.getElasticsearchCluster();
    		    	String elasticsearchNode =object.getElasticsearchNode();
    		    	try {
    		    		Settings settings = Settings.builder()
    		    				.put("cluster.name", elasticsearchCluster)
    		    				.put("node.name", elasticsearchNode)
    		    				.build();
    		    		transportClient = new PreBuiltTransportClient(settings)
    		    				.addTransportAddress(
    		    				new InetSocketTransportAddress(
    		    				new InetSocketAddress(elasticsearchHost, Integer.parseInt(elasticsearchPort))));
    		    	} catch (Exception e) {
    		    		e.printStackTrace();
    		    	}
    			}
    		}
    	}
    	return transportClient;
      }

	@PreDestroy
	public void closeTransportClient() {
		synchronized (ElasticsearchUtil.class) {
			if (transportClient != null) {
				transportClient.close();
				transportClient = null;
			}
		}
	}
}
