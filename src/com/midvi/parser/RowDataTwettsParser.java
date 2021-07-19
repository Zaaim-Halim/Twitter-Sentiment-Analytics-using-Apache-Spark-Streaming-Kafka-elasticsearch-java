package com.midvi.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RowDataTwettsParser implements Serializable {

	private static final long serialVersionUID = 1L;

	public RowDataTwettsParser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tweet tweetToJsonContentToObject(String string) {
		JsonObject jsonObject = new JsonParser().parse(string).getAsJsonObject();
		Tweet tweet = new Tweet();
		Gson gson = new Gson();
		String tweetContent = jsonObject.get("text").getAsString();
		JsonElement e = jsonObject.get("user").getAsJsonObject().get("location");
		String localisaion = null;
		System.out.println("ggggg"+ e.toString());
		if (!(e instanceof JsonNull)) {
			localisaion = jsonObject.get("user").getAsJsonObject().get("location").getAsString();
			
		}
		else 
			localisaion = "NULL";
		String CreationDate = jsonObject.get("created_at").getAsString();
		String userName = jsonObject.get("user").getAsJsonObject().get("name").getAsString();
		int followerCount = Integer
				.parseInt(jsonObject.get("user").getAsJsonObject().get("followers_count").getAsString());
		int retweetCount;
		JsonElement element = jsonObject.get("retweeted_status");
		if (!(element == null)) {
			retweetCount = Integer
					.parseInt(jsonObject.get("retweeted_status").getAsJsonObject().get("retweet_count").getAsString());
		} else
			retweetCount = 0;

		String hatag = jsonObject.get("entities").getAsJsonObject().get("hashtags").toString();
		String trimmdeTags = hatag.substring(1, hatag.length() - 1);

		Hashtag hashtag = null;
		List<Hashtag> hashtags = new ArrayList<>();
		if (hatag.length() > 3) {
			String[] tags = trimmdeTags.split("},");
			for (String s : tags) {
				if (!s.trim().substring(s.length() - 1, s.length()).equals("}")) {
					String ss = s.concat("}");
					s = ss;
				}

				hashtag = gson.fromJson(s, Hashtag.class);
				hashtags.add(hashtag);
			}
		}

		String lang = jsonObject.get("lang").getAsString();
		tweet.setContent(tweetContent);
		tweet.setCreatetionDate(CreationDate);
		tweet.setLang(lang);
		tweet.setHashtags(hashtags);
		tweet.setUserName(userName);
		tweet.setFollowersCount(followerCount);
		tweet.setRetweetCount(retweetCount);
		tweet.setLocation(localisaion);
		// System.out.println(tweet);
		return tweet;
	}

	public boolean isEnglishTweet(String string) {
		JsonObject jsonObject = new JsonParser().parse(string).getAsJsonObject();
		String lang = jsonObject.get("lang").getAsString();
		if (lang.compareToIgnoreCase(lang) == 0)
			return true;
		return false;
	}

}
