package com.midvi.functions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.VoidFunction;

import com.midvi.ElasticSearchFactory;

public class SendLocalizationToElasticSearch implements VoidFunction<JavaPairRDD<String,Integer>>,Serializable{
	private Map<String , Object> bd= new HashMap<>();
	private static final long serialVersionUID = 1L;

	@Override
	public void call(JavaPairRDD<String, Integer> t) throws Exception {
		t.foreach(tuple -> {
			bd.put("localisation", tuple._1());bd.put("l_occurance", tuple._2());
			System.out.println(bd.toString());
			 ElasticSearchFactory.getInstance().sendDataToElasticSearch(bd,"localisation");
		});
		
	}

}
