package com.midvi.functions;

import java.io.Serializable;

import org.apache.spark.api.java.function.Function;

import com.midvi.processing.SentimentAnalyzer;

/**
 *  
 *         takes a String of tweet Content and return a processed
 *         one by removing stop words , special chars ....
 *         @author ZAAIM HALIM
 *
 */
public class Preproccess implements Function<String, String>, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String call(String v1) throws Exception {
		return SentimentAnalyzer.processtweet(v1);
	}

}
