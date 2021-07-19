package com.midvi.functions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.function.VoidFunction;

import com.midvi.ElasticSearchFactory;

import scala.Tuple2;

public class SendMostFrequentWordsToES implements VoidFunction<Tuple2<String,Integer>> ,Serializable{
	private Map<String , Object> bd= new HashMap<>();
	private static final long serialVersionUID = 1L;

	@Override
	public void call(Tuple2<String, Integer> t) throws Exception {
		bd.put("word", t._1());bd.put("w_occurance", t._2());
		 ElasticSearchFactory.getInstance().sendDataToElasticSearch(bd,"frequent-word");
	}

}
