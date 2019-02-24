package me.kcaldwell.hackernewsreader.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;

public class FeedItemDao {

    public static void createFeedItemsFromArray(JSONArray feed, Realm realmInstance) {
        final FeedItem feedItem = new FeedItem();
        realmInstance.executeTransaction(realm -> {
            for (int i = 0; i < feed.length(); i++) {
                try {
                    JSONObject article = feed.getJSONObject(i);
                    feedItem.setId(article.getLong("id"));
                    feedItem.setTitle(article.getString("title"));
                    if (!article.isNull("points") && article.has("points")) {
                        feedItem.setPoints(article.getInt("points"));
                    }
                    if (!article.isNull("user") && article.has("user")) {
                        feedItem.setAuthor(article.getString("user"));
                    }
                    feedItem.setTime(article.getLong("time"));
                    feedItem.setTimeAgo(article.getString("time_ago"));
                    feedItem.setCommentsCount(article.getInt("comments_count"));
                    feedItem.setType(article.getString("type"));
                    if (!article.isNull("url") && article.has("url")) {
                        feedItem.setUrl(article.getString("url"));
                    }
                    if (!article.isNull("domain") && article.has("domain")) {
                        feedItem.setDomain(article.getString("domain"));
                    }
                    realmInstance.copyToRealmOrUpdate(feedItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
