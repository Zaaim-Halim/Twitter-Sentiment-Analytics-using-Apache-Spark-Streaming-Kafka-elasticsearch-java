package com.midvi.functions;

import java.io.Serializable;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;

import com.midvi.parser.Tweet;

public class CleanTweetContent implements Function<JavaRDD<Tweet>, JavaRDD<String>>, Serializable {

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
}
