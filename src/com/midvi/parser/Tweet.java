package com.midvi.parser;

import java.io.Serializable;
import java.util.List;

public class Tweet implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String content;
	private String createtionDate;
	private String UserName;
	private int friendsCount;
	private int followersCount;
	private int retweetCount;
	private List<Hashtag> hashtags;
	private String location;
	private String lang;
	public Tweet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatetionDate() {
		return createtionDate;
	}
	public void setCreatetionDate(String createtionDate) {
		this.createtionDate = createtionDate;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public int getFriendsCount() {
		return friendsCount;
	}
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
	public int getFollowersCount() {
		return followersCount;
	}
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	public int getRetweetCount() {
		return retweetCount;
	}
	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}
	public List<Hashtag> getHashtags() {
		return hashtags;
	}
	public void setHashtags(List<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	@Override
	public String toString() {
		return "Tweet [content=" + content + ", createtionDate=" + createtionDate + ", UserName=" + UserName
				+ ", friendsCount=" + friendsCount + ", followersCount=" + followersCount + ", retweetCount="
				+ retweetCount + ", hashtags=" + hashtags + ", location=" + location + ", lang=" + lang + "]";
	}
	
}
