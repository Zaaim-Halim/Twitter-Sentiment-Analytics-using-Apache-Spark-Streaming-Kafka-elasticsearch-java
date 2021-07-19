package com.midvi.functions;

import java.io.Serializable;

import org.apache.spark.api.java.function.Function;

/**
 * @author ZAAIM HALIM
 * 
 *         Map sentiment prediction to a human readable labels
 *
 */
public class MapPrediction implements Function<Integer, String>, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String call(Integer v1) throws Exception {
		if (v1 == 2)
			return "neutral";
		else if (v1 == 3 || v1 == 4)
			return "positive";
		else if (v1 == 1 || v1 == 0)
			return "negative";

		else
			return "unknown";
	}

}
