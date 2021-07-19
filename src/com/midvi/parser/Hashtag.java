package com.midvi.parser;

import java.util.Arrays;

public class Hashtag {
	private String text;
	private int [] indices;
	
	public String getText() {
		return text;
	}
	
	public Hashtag() {
		super();
	}

	public void setText(String text) {
		this.text = text;
	}
	public int[] getIndices() {
		return indices;
	}
	public void setIndices(int[] indices) {
		this.indices = indices;
	}

	@Override
	public String toString() {
		return "Hashtag [text=" + text + ", indices=" + Arrays.toString(indices) + "]";
	}
	
}
