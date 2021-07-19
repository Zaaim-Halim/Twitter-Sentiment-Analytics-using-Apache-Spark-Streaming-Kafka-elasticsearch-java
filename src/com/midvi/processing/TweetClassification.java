package com.midvi.processing;

import java.io.Serializable;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import com.midvi.functions.SendTweetsClassificationToES;
import com.midvi.parser.Tweet;

import scala.Tuple2;

public class TweetClassification implements Serializable {

	private static final long serialVersionUID = 1L;

	public TweetClassification(JavaDStream<Tweet> tweetDStream) {
		JavaPairDStream<Integer, String> tweetClassificationDStream = tweetDStream
				.mapToPair(new PairFunction<Tweet, Integer, String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<Integer, String> call(Tweet t) throws Exception {

						return new Tuple2<Integer, String>(t.getRetweetCount(), t.getContent());
					}
				});

		tweetClassificationDStream.window(new Duration(5000))
				.foreachRDD(new VoidFunction<JavaPairRDD<Integer, String>>() {

					private static final long serialVersionUID = 1L;

					@Override
					public void call(JavaPairRDD<Integer, String> t) throws Exception {
						t.sortByKey(false).foreach(new SendTweetsClassificationToES());

					}
				});
	}

}
