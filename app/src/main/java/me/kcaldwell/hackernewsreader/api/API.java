package me.kcaldwell.hackernewsreader.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class API {

    private static final String LOG = API.class.getSimpleName();

    public static void post(final Context context,
                            final String url,
                            final String tag,
                            final ResponseCallback responseListener,
                            final ErrorCallback errorListener) {

        // Create request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    Log.i(tag, "Response received from " + url + ": " + response.toString());
                    try {
                        responseListener.onResponse(new JSONArray(response.toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(tag, "A volley error occurred with: " + url);
                    String message = "";
                    if (error != null) {
                        message += error.toString();
                        if (error.networkResponse != null) {
                            message += ". " + error.networkResponse.toString();
                        }
                    }
                    Log.e(tag, "Error message: " + message);
                    errorListener.onError();
                });

        // post the request to queue
        Log.i(LOG, "Making request to: " + url);
        APISingleton.getInstance(context).addToRequestQueue(request);
    }

    static void getArray(final Context context,
                         final String url,
                         final String tag,
                         final ResponseCallback responseListener,
                         final ErrorCallback errorListener) {

        // create request
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.i(tag, "Response received from " + url + ": " + response.toString());
                    responseListener.onResponse(response);
                },
                error -> {
                    Log.e(tag, "A volley error occurred with: " + url);
                    String message = "";
                    if (error != null) {
                        message += error.toString();
                        if (error.networkResponse != null) {
                            message += ". " + error.networkResponse.toString();
                        }
                    }
                    Log.e(tag, "Error message is: " + message);
                    errorListener.onError();
                });
        // post the request to queue
        Log.i(LOG, "Making request to: " + url);
        APISingleton.getInstance(context).addToRequestQueue(request);
    }

    static void getObject(final Context context,
                          final String url,
                          final String tag,
                          final ResponseObjectCallback responseListener,
                          final ErrorCallback errorListener) {

        // create request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.i(tag, "Response received from " + url + ": " + response.toString());
                    responseListener.onResponse(response);
                },
                error -> {
                    Log.e(tag, "A volley error occurred with: " + url);
                    String message = "";
                    if (error != null) {
                        message += error.toString();
                        if (error.networkResponse != null) {
                            message += ". " + error.networkResponse.toString();
                        }
                    }
                    Log.e(tag, "Error message is: " + message);
                    errorListener.onError();
                });
        // post the request to queue
        Log.i(LOG, "Making request to: " + url);
        APISingleton.getInstance(context).addToRequestQueue(request);
    }


    public interface ResponseCallback {
        void onResponse(JSONArray response);
    }

    public interface ResponseObjectCallback {
        void onResponse(JSONObject response);
    }

    public interface ErrorCallback {
        void onError();
    }

}
