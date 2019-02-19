package me.kcaldwell.hackernewsreader.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

class APISingleton {

    private static APISingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private APISingleton(Context context) {
        mContext = context;

        mRequestQueue = getRequestQueue();
    }

    static synchronized APISingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new APISingleton(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
