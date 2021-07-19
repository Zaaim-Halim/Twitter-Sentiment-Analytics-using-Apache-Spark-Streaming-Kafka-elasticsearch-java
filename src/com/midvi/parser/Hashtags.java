package com.midvi.parser;

import java.util.ArrayList;
import java.util.List;

public class Hashtags {
	List<Hashtag> hashTs;

	public List<Hashtag> getHashTs() {
		return hashTs;
	}

	public void setHashTs(List<Hashtag> hashTs) {
		this.hashTs = hashTs;
	}

	public Hashtags(List<Hashtag> hashTs) {
		super();
		this.hashTs = hashTs;
	}

	public Hashtags() {
		hashTs = new ArrayList<Hashtag>();
	}

	@Override
	public String toString() {
		return "Hashtags [hashTs=" + hashTs + "]";
	}
	

}
