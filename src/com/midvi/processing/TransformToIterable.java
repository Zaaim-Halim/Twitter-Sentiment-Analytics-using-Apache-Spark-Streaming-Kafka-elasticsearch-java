package com.midvi.processing;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.api.java.function.Function;

/**
 * @author ZAAIM HALIM
 *  takes a string and transform it to Iterable<String>
 */
public class TransformToIterable implements Function<String, Iterable<String>>, Serializable {

	private static final long serialVersionUID = 7534985429682058093L;

	@Override
	public Iterable<String> call(String v1) throws Exception {
		String[] words = v1.split(" ");
		Iterable<String> output = Arrays.asList(words);
		return output;
	}
}
