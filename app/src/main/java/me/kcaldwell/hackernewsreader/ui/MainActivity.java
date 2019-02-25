package me.kcaldwell.hackernewsreader.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.data.FeedItem;
import me.kcaldwell.hackernewsreader.ui.ArticleListFragment.OnArticleCommentsSelectedListener;
import me.kcaldwell.hackernewsreader.ui.ArticleListFragment.OnArticleSelectedListener;
import me.kcaldwell.hackernewsreader.ui.WebviewFragment.OnArticleBookmarkListener;

/**
 * MainActivity of the app. Displays a list of articles pulled from the HackerNews API
 */
public class MainActivity extends AppCompatActivity implements OnArticleSelectedListener,
        OnArticleBookmarkListener,
        OnArticleCommentsSelectedListener,
        ArticleListFragment.OnArticlesLoadingListener,
        CommentListFragment.OnCommentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FragmentManager mFragmentManager;

    private Toolbar mToolbar;
    private LottieAnimationView mAnimationView;
    private TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mAnimationView = findViewById(R.id.animation_view);
        mTitleTextView = findViewById(R.id.title_text_view);

        mFragmentManager = getSupportFragmentManager();

        loadArticleListFragment();
    }

    private void loadArticleListFragment() {
        mToolbar.setVisibility(View.VISIBLE);

        // Load initial Fragment
        Fragment fragment = new ArticleListFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.commit();
    }

    private void navigateToArticleFragment(String title, String url) {
        if (title != null) {
            mTitleTextView.setText(title);
        } else {
            mToolbar.setVisibility(View.GONE);
        }

        Fragment fragment = new WebviewFragment();

        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right);

        transaction.replace(R.id.container, fragment, "webview");

        // Add the transaction to the back stack to preserve back navigation
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToArticleComments(long id) {
        mToolbar.setVisibility(View.GONE);

        Fragment fragment = new CommentListFragment();

        Bundle bundle = new Bundle();
        bundle.putString("id", String.valueOf(id));
        fragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right);

        transaction.replace(R.id.container, fragment);

        // Add the transaction to the back stack to preserve back navigation
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = mFragmentManager.findFragmentByTag("webview");
        if (currentFragment instanceof WebviewFragment) {
            if (((WebviewFragment) currentFragment).mWebview.canGoBack()) {
                ((WebviewFragment) currentFragment).mWebview.goBack();
            } else {
                mToolbar.setVisibility(View.VISIBLE);
                mTitleTextView.setText(getString(R.string.main_title));
                super.onBackPressed();
            }
        } else {
            mToolbar.setVisibility(View.VISIBLE);
            mTitleTextView.setText(getString(R.string.main_title));
            super.onBackPressed();
        }
    }

    @Override
    public void onArticleSelected(FeedItem item) {
        navigateToArticleFragment(item.getTitle(), item.getUrl());
    }

    @Override
    public void onCommentsSelected(FeedItem item) {
        navigateToArticleComments(item.getId());
    }

    @Override
    public void onArticlesLoading(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mAnimationView.setVisibility(visibility);
    }

    @Override
    public void onArticleBookmarked(Uri uri) {

    }

    @Override
    public void onCommentUrlClicked(String url) {
        navigateToArticleFragment(null, url);
    }
}
