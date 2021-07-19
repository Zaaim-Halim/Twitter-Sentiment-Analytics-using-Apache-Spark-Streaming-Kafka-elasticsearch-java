package com.midvi.functions;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;

import com.midvi.parser.RowDataTwettsParser;
import com.midvi.parser.Tweet;

/**
 * @author ZAAIM HALIM
 * 
 *         get tweets as an readable object from raw ConsumerRecord<String,
 *         String> sent by KAFKA
 *
 */
public class GetTweets implements Function<JavaRDD<ConsumerRecord<String, String>>, JavaRDD<Tweet>> {
	private static final long serialVersionUID = 1L;
	private RowDataTwettsParser parser = new RowDataTwettsParser();

	@Override
	public JavaRDD<Tweet> call(JavaRDD<ConsumerRecord<String, String>> v1) throws Exception {
		return v1.map(new Function<ConsumerRecord<String, String>, Tweet>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Tweet call(ConsumerRecord<String, String> v1) throws Exception {

				return parser.tweetToJsonContentToObject(v1.value());
			}
		});
		
	}

}
