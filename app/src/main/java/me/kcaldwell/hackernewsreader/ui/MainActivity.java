package me.kcaldwell.hackernewsreader.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.data.FeedItem;
import me.kcaldwell.hackernewsreader.data.Item;
import me.kcaldwell.hackernewsreader.ui.ArticleFragment.OnArticleBookmarkListener;
import me.kcaldwell.hackernewsreader.ui.ArticleListFragment.OnArticleCommentsSelectedListener;
import me.kcaldwell.hackernewsreader.ui.ArticleListFragment.OnArticleSelectedListener;
import me.kcaldwell.hackernewsreader.ui.CommentFragment.OnListFragmentInteractionListener;

/**
 * MainActivity of the app. Displays a list of articles pulled from the HackerNews API
 */
public class MainActivity extends AppCompatActivity implements OnArticleSelectedListener,
                                                               OnArticleBookmarkListener,
                                                               OnArticleCommentsSelectedListener,
                                                               OnListFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FragmentManager mFragmentManager;

    private TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mTitleTextView = findViewById(R.id.title_text_view);

        mFragmentManager = getSupportFragmentManager();

        loadArticleListFragment();
    }

    private void loadArticleListFragment() {
        // Load initial Fragment
        Fragment fragment = new ArticleListFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.commit();
    }

    private void navigateToArticleFragment(String title, String url) {
        mTitleTextView.setText(title);

        Fragment fragment = new ArticleFragment();

        Bundle bundle = new Bundle();
        bundle.putString("url", url);
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

    private void navigateToArticleComments(String title, long id) {
        mTitleTextView.setText(title);

        Fragment fragment = new CommentFragment();

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
        mTitleTextView.setText(getString(R.string.main_title));
        super.onBackPressed();
    }

    @Override
    public void onArticleSelected(FeedItem item) {
        navigateToArticleFragment(item.getTitle(), item.getUrl());
    }

    @Override
    public void onCommentsSelected(FeedItem item) {
        navigateToArticleComments(item.getTitle(), item.getId());
    }

    @Override
    public void onArticleBookmarked(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Item item) {

    }
}
