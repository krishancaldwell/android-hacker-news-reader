package me.kcaldwell.hackernewsreader.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

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
    private ArticleRecyclerViewAdapter mAdapter;
    private View mLoadingView;
    private LottieAnimationView mAnimationView;

    private int mPreviousTotal = 0;
    private boolean mLoading = true;
    private int mVisibleThreshold = 5;
    private int mPage = 1;
    int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

    private OnArticleSelectedListener mArticleListener;
    private OnArticleCommentsSelectedListener mCommentsListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ArticleListFragment newInstance(int columnCount) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_list, container, false);

        mLoadingView = rootView.findViewById(R.id.loading_mask);
        mAnimationView = rootView.findViewById(R.id.animation_view);
        RecyclerView recyclerView = rootView.findViewById(R.id.list);

        mRealm = Realm.getDefaultInstance();

        // Remove stale data from local DB
        removeStaleDataFromRealm();

        // Fetch current data
        getArticles();

        // Set the adapter
        Context context = rootView.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        RealmResults<FeedItem> articles = mRealm.where(FeedItem.class).findAll();

        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ArticleRecyclerViewAdapter(articles, mArticleListener, mCommentsListener);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


        return rootView;
    }

    private void removeStaleDataFromRealm() {
        long oneHourAgo = 3600000L;
        long now = System.currentTimeMillis();
        long yesterday = now - oneHourAgo;

        RealmResults<FeedItem> staleItems = mRealm.where(FeedItem.class)
                .lessThan("time", yesterday)
                .findAll();
        if (staleItems.size() > 0) {
            mRealm.executeTransaction(realm -> staleItems.deleteAllFromRealm());
        }
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
            throw new RuntimeException(context.toString() + " must implement OnArticleCommentsSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mArticleListener = null;
        mCommentsListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRealm != null) {
            mRealm.close();
        }
    }

    // Get articles
    private void getArticles() {
        toggleProgressViews(true);

        News.get(getActivity(), mPage, response -> {
            FeedItemDao.createFeedItemsFromArray(response, mRealm);
            refreshArticles();
            toggleProgressViews(false);

        }, () -> {
            Log.e(TAG, "An error occurred");

            Toast.makeText(getActivity(), "There was an error updating the stories", Toast.LENGTH_LONG).show();
        });
    }

    private void toggleProgressViews(boolean show) {
        int visible = show ? View.VISIBLE : View.GONE;
        mLoadingView.setVisibility(visible);
        mAnimationView.setVisibility(visible);
    }

    private void refreshArticles() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnArticleSelectedListener {
        // TODO: Update argument type and name
        void onArticleSelected(FeedItem feedItem);
    }

    public interface OnArticleCommentsSelectedListener {
        void onCommentsSelected(FeedItem feedItem);
    }
}
