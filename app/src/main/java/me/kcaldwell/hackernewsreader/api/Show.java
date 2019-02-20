package me.kcaldwell.hackernewsreader.api;

import android.content.Context;

import me.kcaldwell.hackernewsreader.utils.Constants;

public class Show {

    private static final String LOG = Show.class.getSimpleName();
    private static final String url = Constants.URL_SHOW;

    public static void get(final Context context,
                           final API.ResponseCallback responseCallback,
                           final API.ErrorCallback errorCallback) {

        API.getArray(context, url, LOG,
                response -> responseCallback.onResponse(response),
                () -> errorCallback.onError());
    }

}
