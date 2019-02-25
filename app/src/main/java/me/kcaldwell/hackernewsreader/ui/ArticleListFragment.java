package me.kcaldwell.hackernewsreader.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.adapters.ArticleRecyclerViewAdapter;
import me.kcaldwell.hackernewsreader.api.News;
import me.kcaldwell.hackernewsreader.data.FeedItem;
import me.kcaldwell.hackernewsreader.data.FeedItemDao;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnArticleSelectedListener}
 * interface.
 */
public class ArticleListFragment extends Fragment {

    private static final String TAG = ArticleListFragment.class.getSimpleName();

    private Realm mRealm;
    private RecyclerView mRecyclerView;
    private ArticleRecyclerViewAdapter mAdapter;
    private View mLoadingView;
    private LottieAnimationView mAnimationView;

    private Parcelable mRecyclerViewState;
    private int mPreviousTotal = 0;
    private boolean mLoading = true;
    private int mVisibleThreshold = 5;
    private int mPage = 1;
    private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

    private OnArticleSelectedListener mArticleListener;
    private OnArticleCommentsSelectedListener mCommentsListener;
    private OnArticlesLoadingListener mLoadingListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_list, container, false);

        mLoadingView = rootView.findViewById(R.id.loading_mask);
        mAnimationView = rootView.findViewById(R.id.animation_view);
        mRecyclerView = rootView.findViewById(R.id.list);

        mRealm = Realm.getDefaultInstance();

        // Remove old data from local DB
        FeedItemDao.deleteAllFeedItems(mRealm);

        // Fetch current data
        getArticles();

        // Set the adapter
        Context context = rootView.getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        RealmResults<FeedItem> articles = FeedItemDao.getAllFeedItems(mRealm);

        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ArticleRecyclerViewAdapter(articles, mArticleListener, mCommentsListener);
        mRecyclerView.setAdapter(mAdapter);

        setInfiniteScrollingSettings(layoutManager);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnArticleSelectedListener) {
            mArticleListener = (OnArticleSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnArticleSelectedListener");
        }
        if (context instanceof OnArticleCommentsSelectedListener) {
            mCommentsListener = (OnArticleCommentsSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnArticleCommentsSelectedListener");
        }
        if (context instanceof OnArticlesLoadingListener) {
            mLoadingListener = (OnArticlesLoadingListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnArticlesLoadingListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Save the state of the recycler view so the scroll position can be preserved
        mRecyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mArticleListener = null;
        mCommentsListener = null;
        mLoadingListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRealm != null) {
            mRealm.close();
        }
    }

    private void setInfiniteScrollingSettings(LinearLayoutManager linearLayoutManager) {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for downwards scrolling
                    mVisibleItemCount = linearLayoutManager.getChildCount();
                    mTotalItemCount = linearLayoutManager.getItemCount();
                    mFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                    if (mLoading) {
                        if (mTotalItemCount > mPreviousTotal) {
                            mLoading = false;
                            mPreviousTotal = mTotalItemCount;
                        }
                    } else if ((mTotalItemCount - mVisibleItemCount) <= (mFirstVisibleItem + mVisibleThreshold)) {
                        // End has been reached
                        Log.i(TAG, "End of list reached");

                        // Do something
                        mLoading = true;
                        mPage++;
                        getArticles();
                    }
                }
            }
        });
    }

    private void getArticles() {
        toggleProgressViews(true);

        News.get(getActivity(), mPage, response -> {
            FeedItemDao.createOrUpdateFeedItemsFromArray(response, mRealm);
            refreshAdapterArticles();
            scrollToPositionIfSet();
            toggleProgressViews(false);
        }, () -> {
            Log.e(TAG, "An error occurred");
            toggleProgressViews(false);
            Toast.makeText(getActivity(), "There was an error updating the stories", Toast.LENGTH_LONG).show();
        });
    }

    private void scrollToPositionIfSet() {
        if (mRecyclerViewState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerViewState);
        } else {
            Log.i(TAG, "State was null");
        }
    }

    private void toggleProgressViews(boolean show) {
        if (mPage < 2) {
            int visible = show ? View.VISIBLE : View.GONE;
            mLoadingView.setVisibility(visible);
            mAnimationView.setVisibility(visible);
            mLoadingListener.onArticlesLoading(show);
        }
        else {
            mLoadingListener.onArticlesLoading(show);
        }
    }

    private void refreshAdapterArticles() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Interfaces for the Adapter to implement. This enables callbacks to be passed and appropriate
     * action to be taken by the Activity.
     */
    public interface OnArticleSelectedListener {
        void onArticleSelected(FeedItem feedItem);
    }

    public interface OnArticleCommentsSelectedListener {
        void onCommentsSelected(FeedItem feedItem);
    }

    public interface OnArticlesLoadingListener {
        void onArticlesLoading(boolean show);
    }
}
