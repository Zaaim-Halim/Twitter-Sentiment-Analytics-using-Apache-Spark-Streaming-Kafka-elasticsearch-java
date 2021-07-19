package com.midvi.processing;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import com.midvi.functions.SendHashTagToElasticSearch;

import scala.Tuple2;
import scala.Tuple3;

public class MostUsedHashTags implements Serializable {

	private static final long serialVersionUID = 1L;

	public void Process(JavaDStream<Tuple3<String, String, String>> htagsDStream) {
		JavaDStream<String> hashTagRDD = htagsDStream
				.transform(new Function<JavaRDD<Tuple3<String, String, String>>, JavaRDD<List<String>>>() {

					private static final long serialVersionUID = 1L;

					@Override
					public JavaRDD<List<String>> call(JavaRDD<Tuple3<String, String, String>> v1) throws Exception {
						return v1.map(new Function<Tuple3<String, String, String>, List<String>>() {
							private static final long serialVersionUID = 1L;

							@Override
							public List<String> call(Tuple3<String, String, String> v1) throws Exception {
								return Stream.of(v1._3().substring(1, v1._3().length() - 1).split(", "))
										.collect(Collectors.toList());

							}
						});
					}
				}).transform(new Function<JavaRDD<List<String>>, JavaRDD<String>>() {

					private static final long serialVersionUID = 1L;

					@Override
					public JavaRDD<String> call(JavaRDD<List<String>> v1) throws Exception {

						return v1.flatMap(new FlatMapFunction<List<String>, String>() {

							private static final long serialVersionUID = 1L;

							@Override
							public Iterator<String> call(List<String> t) throws Exception {

								return t.iterator();
							}
						});
					}
				});

		JavaPairDStream<Integer, String> hashtagCountsRDD = hashTagRDD
				.mapToPair(hashTag -> new Tuple2<String, Integer>("#"+hashTag, 1))
				.reduceByKeyAndWindow((integer1, integer2) -> (integer1 + integer2), Durations.seconds(5000)) // 15 min
				.mapToPair(tuple -> tuple.swap());

		   
		
		hashtagCountsRDD.foreachRDD(new VoidFunction<JavaPairRDD<Integer, String>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(JavaPairRDD<Integer, String> hCountsRDD) throws Exception {

				hCountsRDD.foreach(new SendHashTagToElasticSearch());
			
			}
		});
	}

}
