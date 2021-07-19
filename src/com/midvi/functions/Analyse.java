package com.midvi.functions;

import java.io.Serializable;

import org.apache.spark.api.java.function.Function;

import com.midvi.processing.SentimentAnalyzer;

public class Analyse implements Function<String, Integer>, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Integer call(String v1) throws Exception {

		return SentimentAnalyzer.analyse(v1);
	}

}
