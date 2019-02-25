package me.kcaldwell.hackernewsreader.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;

public class CommentDao {

    private static final String TAG = CommentDao.class.getSimpleName();

    public static void createCommentsFromArray(JSONArray comments, Realm realmInstance, @Nullable Long parentId) {
        final Comment comment = new Comment();
        for (int i = 0; i < comments.length(); i++) {
            try {
                JSONObject jsonObject = comments.getJSONObject(i);

                comment.setId(jsonObject.getLong("id"));
                if (jsonObject.has("user")) {
                    comment.setAuthor(jsonObject.getString("user"));
                } else {
                    comment.setAuthor("Deleted User");
                }
                comment.setTime(jsonObject.getLong("time"));
                comment.setTimeAgo(jsonObject.getString("time_ago"));
                comment.setContent(jsonObject.getString("content"));
                comment.setType(jsonObject.getString("type"));
                comment.setUrl(jsonObject.getString("url"));
                comment.setCommentsCount(jsonObject.getInt("comments_count"));
                if (jsonObject.has("level")) {
                    comment.setLevel(jsonObject.getInt("level"));
                }
                else {
                    comment.setLevel(0);
                }
                realmInstance.copyToRealmOrUpdate(comment);

                if (parentId != null) {
                    Comment parent = realmInstance.where(Comment.class).equalTo("id", parentId).findFirst();
                    if (parent == null) {
                        return;
                    }

                    parent.getComments().add(comment);
                    realmInstance.copyToRealmOrUpdate(parent);
                }

                JSONArray childComments = jsonObject.getJSONArray("comments");
                if (comments.length() > 0) {
                    createCommentsFromArray(childComments, realmInstance, comment.getId());
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSON Exception: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static RealmResults<Comment> getAllComments(Realm realmInstance) {
        return realmInstance.where(Comment.class).findAll();
    }

    public static void removeAllComments(Realm realmInstance) {
        RealmResults<Comment> oldComments = realmInstance.where(Comment.class).findAll();
        realmInstance.executeTransaction(realm -> oldComments.deleteAllFromRealm());
    }

}
