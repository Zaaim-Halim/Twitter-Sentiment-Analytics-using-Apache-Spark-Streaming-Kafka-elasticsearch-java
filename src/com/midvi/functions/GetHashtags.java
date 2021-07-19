package com.midvi.functions;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;

import com.midvi.parser.Tweet;

import scala.Tuple3;

/**
 * @author ZAAIM HALIM
 *  first filter tweets and takes only tweets that contains
 *  hashtags then return = (tweet, username, "Hashtag1 hashtag2 ...") of
 *  type Tuple3(String,String,String)
 *
 */

public class GetHashtags implements Function<JavaRDD<Tweet>, JavaRDD<Tuple3<String, String, String>>> {

	private static final long serialVersionUID = 1L;

	@Override
	public JavaRDD<Tuple3<String, String, String>> call(JavaRDD<Tweet> v1) throws Exception {
		return v1.filter(f -> !f.getHashtags().isEmpty())
				.flatMap((FlatMapFunction<Tweet, Tuple3<String, String, String>>) m -> {
					return Arrays
							.asList(new Tuple3<String, String, String>(m.getContent(), m.getUserName(), m.getHashtags()
									.stream().map(h -> h.getText()).collect(Collectors.toList()).toString()))
							.iterator();
				});
	}

}
