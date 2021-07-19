package com.midvi.processing;

import java.io.Serializable;

public class data implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String text;
	private Double Sentiment_Class;
	public data() {
		super();
		// TODO Auto-generated constructor stub
	}
	public data(String text, Double sentiment_Class) {
		super();
		this.text = text;
		Sentiment_Class = sentiment_Class;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Double getSentiment_Class() {
		return Sentiment_Class;
	}
	public void setSentiment_Class(Double sentiment_Class) {
		Sentiment_Class = sentiment_Class;
	}
	@Override
	public String toString() {
		return "data [text=" + text + ", Sentiment_Class=" + Sentiment_Class + "]";
	}
	
	
}