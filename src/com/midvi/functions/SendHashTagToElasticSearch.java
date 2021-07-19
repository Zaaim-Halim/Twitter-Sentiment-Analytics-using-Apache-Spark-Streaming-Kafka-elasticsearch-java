package com.midvi.functions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.function.VoidFunction;

import com.midvi.ElasticSearchFactory;

import scala.Tuple2;

public class SendHashTagToElasticSearch implements VoidFunction<Tuple2<Integer, String>>, Serializable {
	private Map<String, Object> bd = new HashMap<>();
	private static final long serialVersionUID = 1L;

	@Override
	public void call(Tuple2<Integer, String> t) throws Exception {
		bd.put("h_occurance", t._1());
		bd.put("hashtag", t._2());
		ElasticSearchFactory.getInstance().sendDataToElasticSearch(bd, "hashtags");
	}

}
