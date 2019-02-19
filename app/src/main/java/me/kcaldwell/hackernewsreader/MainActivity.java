package me.kcaldwell.hackernewsreader;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import me.kcaldwell.hackernewsreader.ArticleListFragment.OnArticleSelectedListener;
import me.kcaldwell.hackernewsreader.dummy.DummyContent;

/**
 * MainActivity of the app. Displays a list of articles pulled from the HackerNews API
 */
public class MainActivity extends AppCompatActivity implements OnArticleSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

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

    @Override
    public void onArticleSelected(DummyContent.DummyItem item) {

    }
}
