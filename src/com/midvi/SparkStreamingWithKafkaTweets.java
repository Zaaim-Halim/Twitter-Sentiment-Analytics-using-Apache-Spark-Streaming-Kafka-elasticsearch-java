package com.midvi;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.CanCommitOffsets;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.HasOffsetRanges;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.kafka010.OffsetRange;
import com.midvi.functions.GetHashtags;
import com.midvi.parser.RowDataTwettsParser;
import com.midvi.parser.Tweet;
import com.midvi.processing.MostFrequentWord;
import com.midvi.processing.MostUsedHashTags;
import com.midvi.processing.SentimentAnalyzer;
import com.midvi.processing.TweetClassification;
import com.midvi.processing.TweetLocalisation;

import scala.Tuple3;

public class SparkStreamingWithKafkaTweets {

	private static final String HADOOP_HOME_DIR_VALUE = "C:/winutils";

	private static final String RUN_LOCAL_WITH_AVAILABLE_CORES = "local[*]";
	private static final String APPLICATION_NAME = "Tweets analysis of covid-19 vaccination";
	private static final String CASE_SENSITIVE = "false";

	private static final int BATCH_DURATION_INTERVAL_MS = 5000;

	private static final Map<String, Object> KAFKA_CONSUMER_PROPERTIES;

	private static final String KAFKA_BROKERS = "localhost:9092";
	private static final String KAFKA_OFFSET_RESET_TYPE = "latest";
	private static final String KAFKA_GROUP = "covid-19Group";
	private static final String KAFKA_TOPIC = "covid-19";
	private static final Collection<String> TOPICS = Collections.unmodifiableList(Arrays.asList(KAFKA_TOPIC));

	static {
		Map<String, Object> kafkaProperties = new HashMap<>();
		kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
		kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, KAFKA_GROUP);
		kafkaProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KAFKA_OFFSET_RESET_TYPE);
		kafkaProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

		KAFKA_CONSUMER_PROPERTIES = Collections.unmodifiableMap(kafkaProperties);
	}

	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(new TwitterKafkaProducer());
		t.start();
		System.setProperty("hadoop.home.dir", HADOOP_HOME_DIR_VALUE);

		final SparkConf conf = new SparkConf().setMaster(RUN_LOCAL_WITH_AVAILABLE_CORES).setAppName(APPLICATION_NAME)
				.set("spark.sql.caseSensitive", CASE_SENSITIVE);
		
		JavaStreamingContext streamingContext = new JavaStreamingContext(conf,
				new Duration(BATCH_DURATION_INTERVAL_MS));
		JavaSparkContext sc = streamingContext.sparkContext();
		
		JavaInputDStream<ConsumerRecord<String, String>> covidTweetStream = KafkaUtils.createDirectStream(
				streamingContext, LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(TOPICS, KAFKA_CONSUMER_PROPERTIES));

		JavaDStream<ConsumerRecord<String, String>> englishCovidTweetStream = covidTweetStream.transform(
				new Function<JavaRDD<ConsumerRecord<String, String>>, JavaRDD<ConsumerRecord<String, String>>>() {

					private static final long serialVersionUID = 1L;
                    
					// TAKE ONLY ENGLISH TWEETS
					@Override
					public JavaRDD<ConsumerRecord<String, String>> call(JavaRDD<ConsumerRecord<String, String>> v1)
							throws Exception {

						return v1.filter(f -> f.value().contains("\"lang\"" + ":\"en\""));
						
					}
				});

		// transform the records to a readable Stream of object "Tweet"
		JavaDStream<Tweet> tweetsDStream = englishCovidTweetStream
				.flatMap((FlatMapFunction<ConsumerRecord<String, String>, Tweet>) s -> {
					RowDataTwettsParser parser = new RowDataTwettsParser();
					return Arrays.asList(parser.tweetToJsonContentToObject(s.value())).iterator();

				});
        
		// take only hashTags from the tweet stream
		JavaDStream<Tuple3<String, String, String>> htagsDStream = tweetsDStream.transform(new GetHashtags());

		/*========================= processes tweets ========================*/
		new MostUsedHashTags().Process(htagsDStream);
		new MostFrequentWord().frequentWords(tweetsDStream);
		new TweetClassification(tweetsDStream);
		new SentimentAnalyzer(sc).predict(tweetsDStream);
		new TweetLocalisation(tweetsDStream);
        /*====================================================================*/
		
		covidTweetStream.foreachRDD((JavaRDD<ConsumerRecord<String, String>> covidTweetsRDD) -> {
			OffsetRange[] offsetRanges = ((HasOffsetRanges) covidTweetsRDD.rdd()).offsetRanges();

			((CanCommitOffsets) covidTweetStream.inputDStream()).commitAsync(offsetRanges,
					new Covid19OffsetCommitCallback());
		});

		streamingContext.start();
		streamingContext.awaitTermination();
	}
}

final class Covid19OffsetCommitCallback implements OffsetCommitCallback {

	private static final Logger log = Logger.getLogger(Covid19OffsetCommitCallback.class.getName());

	@Override
	public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
		log.info("---------------------------------------------------");
		log.log(Level.INFO, "{0} | {1}", new Object[] { offsets, exception });
		log.info("---------------------------------------------------");
	}

}
