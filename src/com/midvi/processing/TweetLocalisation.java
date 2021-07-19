package com.midvi.processing;

import java.io.Serializable;

import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import com.midvi.functions.SendLocalizationToElasticSearch;
import com.midvi.functions.TweetToLocalisation;
import com.midvi.parser.Tweet;

import scala.Tuple2;

public class TweetLocalisation implements Serializable{


	private static final long serialVersionUID = 1L;

	public TweetLocalisation(JavaDStream<Tweet> tweetDStream) {
		JavaDStream<String> localisationDstream = tweetDStream.transform(new TweetToLocalisation());
		
		JavaPairDStream<String, Integer> pairdStream = localisationDstream.mapToPair(localisation -> new Tuple2<String, Integer>(localisation, 1))
				.reduceByKeyAndWindow((integer1, integer2) -> (integer1 + integer2), Durations.seconds(5000)); // 15 min
				
		pairdStream.foreachRDD(new SendLocalizationToElasticSearch());
	}
	

}
