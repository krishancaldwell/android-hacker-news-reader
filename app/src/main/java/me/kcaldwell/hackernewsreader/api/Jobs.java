package me.kcaldwell.hackernewsreader.api;

import android.content.Context;

import me.kcaldwell.hackernewsreader.utils.Constants;

public class Jobs {

    private static final String LOG = Jobs.class.getSimpleName();
    private static final String url = Constants.URL_JOBS;

    public static void get(final Context context,
                           final API.ResponseCallback responseCallback,
                           final API.ErrorCallback errorCallback) {

        API.getArray(context, url, LOG,
                response -> responseCallback.onResponse(response),
                () -> errorCallback.onError());
    }

}
