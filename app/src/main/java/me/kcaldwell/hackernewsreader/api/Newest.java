package me.kcaldwell.hackernewsreader.api;

import android.content.Context;

import me.kcaldwell.hackernewsreader.utils.Constants;

public class Newest {

    private static final String LOG = Newest.class.getSimpleName();
    private static final String url = Constants.URL_NEWEST;

    public static void get(final Context context,
                           final API.ResponseCallback responseCallback,
                           final API.ErrorCallback errorCallback) {

        API.get(context, url, LOG,
                response -> responseCallback.onResponse(response),
                () -> errorCallback.onError());
    }

}
