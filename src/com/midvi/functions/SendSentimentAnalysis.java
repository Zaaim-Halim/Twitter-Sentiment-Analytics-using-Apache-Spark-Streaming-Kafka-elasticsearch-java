package com.midvi.functions;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.function.VoidFunction;
import com.midvi.ElasticSearchFactory;
import scala.Tuple2;

public class SendSentimentAnalysis implements VoidFunction<Tuple2<Integer, String>>{
	private static final long serialVersionUID = 1L;
    private Map<String , Object> bd= new HashMap<>();
	@Override
	public void call(Tuple2<Integer, String> t) throws Exception {
	    bd.put("occurance", t._1());bd.put("label", t._2());
		ElasticSearchFactory.getInstance().sendDataToElasticSearch(bd,"sentiment");
		System.out.println(t._1() +" classification : "+t._2() + "\n");
	}

}
