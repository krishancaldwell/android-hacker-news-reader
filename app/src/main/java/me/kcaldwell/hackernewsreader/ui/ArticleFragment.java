package me.kcaldwell.hackernewsreader.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.utils.HNWebviewClient;


/**
 * A simple {@link Fragment} subclass for loading an article.
 * Activities that contain this fragment must implement the
 * {@link OnArticleBookmarkListener} interface
 * to handle interaction events.
 * Use the {@link ArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_URL = "url";

    // TODO: Rename and change types of parameters
    private String mURL;

    private OnArticleBookmarkListener mListener;

    private ProgressBar mProgressBar;
    private WebView mWebview;

    public ArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleFragment newInstance(String param1) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mURL = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_article, container, false);

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnArticleBookmarkListener {
        void onArticleBookmarked(Uri uri);
    }
}
