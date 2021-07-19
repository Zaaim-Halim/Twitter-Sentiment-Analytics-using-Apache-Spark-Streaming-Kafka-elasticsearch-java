package com.midvi.parser;

public class Entitties {
	
	private Hashtags hashtags;

	public Hashtags getHashtags() {
		return hashtags;
	}

	public void setHashtags(Hashtags hashtags) {
		this.hashtags = hashtags;
	}

	public Entitties(Hashtags hashtags) {
		super();
		this.hashtags = hashtags;
	}

	public Entitties() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Entitties [hashtags=" + hashtags + "]";
	}
	

}
