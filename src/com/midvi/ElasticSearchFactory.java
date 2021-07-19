package com.midvi;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;

public class ElasticSearchFactory {
	  private static ElasticSearchFactory instance;
	  private static RestHighLevelClient client;
	  Logger loger = Logger.getLogger("org.elasticsearch.spark");
	
	  public ElasticSearchFactory() {
      client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
				
	}
	public void CreateIndex(String index) throws IOException {
		
		CreateIndexRequest request = new CreateIndexRequest(index);
		request.settings(Settings.builder().put("index.number_of_shards", 2).put("index.number_of_replicas", 2));
		CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
		System.out.println("response - index Created : " + createIndexResponse.index());
	
	}
	public void sendDataToElasticSearch(Map<String , Object> obj,String index) {
		GetIndexRequest request = new GetIndexRequest().indices(index);
		IndexRequest indexRequest = new IndexRequest(index);
		try {
			@SuppressWarnings("deprecation")
			boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
			if(exists == false)
			{
				this.CreateIndex(index);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String id = UUID.randomUUID().toString();
		indexRequest.id(id);
		indexRequest.source(obj, XContentType.JSON);
		client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
		    @Override
		    public void onResponse(IndexResponse indexResponse) {
		    	
		    	 ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
		    	 if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
			    	 System.out.println("sent "+shardInfo.getSuccessful()+"  of :"+ obj.toString()); 
		    	 }
		    	 if (shardInfo.getFailed() > 0) {
		    	     for (ReplicationResponse.ShardInfo.Failure failure :
		    	             shardInfo.getFailures()) {
		    	         String reason = failure.reason();
		    	         System.out.println(reason);
		    	     }
		    	 }
		    }

		    @Override
		    public void onFailure(Exception e) {
		        System.out.println("  failure while sending data .... :" + e.getMessage());
		    }
		}); 
		
	}
	public void sendDataToElasticSearchASMAP(Map<String , String> obj,String index) {
		  String id = UUID.randomUUID().toString();
		   IndexRequest indexRequest = new IndexRequest(index)
		    .id(id).source(obj); 
		
		client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
		    @Override
		    public void onResponse(IndexResponse indexResponse) {
		   
		    	 ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
		    	 if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
		    	    System.out.println("sent "+shardInfo.getSuccessful()+"  of :"+ obj.toString()); 
		    	 }
		    	 if (shardInfo.getFailed() > 0) {
		    	     for (ReplicationResponse.ShardInfo.Failure failure :
		    	             shardInfo.getFailures()) {
		    	         String reason = failure.reason();
		    	         System.out.println(reason);
		    	     }
		    	 }
		    }

		    @Override
		    public void onFailure(Exception e) {
		        System.out.println("  failure while sending data .... :" + e.getMessage());
		    }
		}); 
	}
	
	public static ElasticSearchFactory getInstance()
	    {
			if(instance == null)
				instance = new ElasticSearchFactory();
			return instance;	
	    } 
}
