package com.midvi.processing;

import java.io.Serializable;
import java.util.Iterator;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import com.midvi.functions.SendMostFrequentWordsToES;
import com.midvi.parser.Tweet;
import scala.Tuple2;

public class MostFrequentWord implements Serializable{
	

	private static final long serialVersionUID = 1L;

	public void frequentWords(JavaDStream<Tweet> tweetDStream) {
		JavaDStream<String> contentDStream = tweetDStream.transform(new Function<JavaRDD<Tweet>, JavaRDD<String>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public JavaRDD<String> call(JavaRDD<Tweet> v1) throws Exception {

				return v1.map(new Function<Tweet, String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String call(Tweet v1) throws Exception {

						return v1.getContent();
					}
				});
			}
		});
		JavaDStream<String> processedTweetContentDStream = contentDStream.map(new Function<String, String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public String call(String v1) throws Exception {
				// takes a tweet and return a processed tweet 
				// removing stop words , special chars ,empty space ....
				// 
				return SentimentAnalyzer.processtweet(v1);
			}
		});
		
		
		
		JavaPairDStream<String,Integer> wordCountDstream = processedTweetContentDStream.flatMapToPair(new PairFlatMapFunction<String, String, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<Tuple2<String, Integer>> call(String t) throws Exception {
				
				return Stream.of(t.split(" ")).map(s -> new Tuple2<String,Integer>(s,1)).iterator();
			}
		}).reduceByKeyAndWindow(new org.apache.spark.api.java.function.Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		}, new Duration(5000));
		
		// send the result to ES
		wordCountDstream.foreachRDD(new VoidFunction<JavaPairRDD<String,Integer>>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void call(JavaPairRDD<String, Integer> t) throws Exception {
				t.foreach(new SendMostFrequentWordsToES());
			}
		});
		
	}
}
