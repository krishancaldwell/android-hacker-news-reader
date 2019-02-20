package me.kcaldwell.hackernewsreader.utils;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HNWebviewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        // Allow the webview to handle loading all sites.
        return false;
    }

}
