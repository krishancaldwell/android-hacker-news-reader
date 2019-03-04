package me.kcaldwell.hackernewsreader.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.utils.Constants;
import me.kcaldwell.hackernewsreader.utils.HNWebviewClient;


/**
 * A simple {@link Fragment} subclass for loading an article.
 * Activities that contain this fragment must implement the
 * {@link OnArticleBookmarkListener} interface
 * to handle interaction events.
 */
public class WebviewFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_TYPE = "type";
    private static final String ARG_URL = "url";

    private String mTitle;
    private String mType;
    private String mURL;

    private OnArticleBookmarkListener mListener;

    private ProgressBar mProgressBar;
    WebView mWebview;

    public WebviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
            mURL = getArguments().getString(ARG_URL);
            if (getArguments().getString(ARG_TITLE) != null) {
                mTitle = getArguments().getString(ARG_TITLE);
            }

            if (mType.equals("ask")) mURL = Constants.EXTERNAL_BASE_URL + mURL;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_article, container, false);
        if (mType.equals("link")) setHasOptionsMenu(true);

        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mWebview = rootView.findViewById(R.id.webview);

        mWebview.loadUrl(mURL);

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        // Set the colour of the progress bar
        mProgressBar.setProgressTintList(ColorStateList
                .valueOf(getResources().getColor(R.color.color_primary)));
        mProgressBar.setMax(100);
        mProgressBar.setProgress(1);

        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }
        });

        // Allows the WebView to handle links that may be presented in the article. Without this,
        // the app would attempt to open the native browser to handle it.
        mWebview.setWebViewClient(new HNWebviewClient());

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onArticleBookmarked(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebview.onResume();
        mWebview.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebview.onPause();
        mWebview.pauseTimers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebview.destroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnArticleBookmarkListener) {
            mListener = (OnArticleBookmarkListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnArticleBookmarkListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_article, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = mTitle + " - " + mURL;
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnArticleBookmarkListener {
        void onArticleBookmarked(Uri uri);
    }
}
