package me.kcaldwell.hackernewsreader.api;

import android.content.Context;

import me.kcaldwell.hackernewsreader.utils.Constants;

public class News {

    private static final String LOG = News.class.getSimpleName();

    public static void get(final Context context,
                           final int page,
                           final API.ResponseCallback responseCallback,
                           final API.ErrorCallback errorCallback) {

        String url = Constants.URL_NEWS + page + Constants.URL_END;

        API.getArray(context, url, LOG,
                response -> responseCallback.onResponse(response),
                () -> errorCallback.onError());
    }

}
