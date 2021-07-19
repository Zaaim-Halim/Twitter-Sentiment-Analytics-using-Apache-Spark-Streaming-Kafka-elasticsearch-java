package com.midvi.functions;

import java.io.Serializable;

import org.apache.spark.api.java.function.Function;

import com.midvi.parser.Tweet;

public class MapTweetToLocation implements Function<Tweet, String>, Serializable{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String call(Tweet v1) throws Exception {
		if (v1.getLocation() != null) {
			if (v1.getLocation().equalsIgnoreCase("NULL"))
				return "unspecified";
		}
		return v1.getLocation();
	}

}
