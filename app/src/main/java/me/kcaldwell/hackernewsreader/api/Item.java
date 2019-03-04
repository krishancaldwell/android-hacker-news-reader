package me.kcaldwell.hackernewsreader.api;

import android.content.Context;

import org.json.JSONObject;

import me.kcaldwell.hackernewsreader.utils.Constants;


/**
 * Returns information for a specific item. I.e. if the provided ID is a news story, this will return
 * the story, plus any additional comments.
 */
public class Item {

    private static final String LOG = Item.class.getSimpleName();

    public static void get(final Context context,
                           final String id,
                           final API.ResponseObjectCallback responseCallback,
                           final API.ErrorCallback errorCallback) {

        String url = Constants.URL_ITEM + id + Constants.URL_END;

        API.getObject(
                context,
                url,
                LOG,
                response -> responseCallback.onResponse(response),
                () -> errorCallback.onError()
        );
    }

}
