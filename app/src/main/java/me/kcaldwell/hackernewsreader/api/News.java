package me.kcaldwell.hackernewsreader.api;

import android.content.Context;

import me.kcaldwell.hackernewsreader.utils.Constants;

public class News {

    private static final String LOG = News.class.getSimpleName();
    private static final String url = Constants.URL_NEWS;

    public static void get(final Context context,
                           final API.ResponseCallback responseCallback,
                           final API.ErrorCallback errorCallback) {

        API.get(context, url, LOG,
                response -> responseCallback.onResponse(response),
                () -> errorCallback.onError());
    }

}
