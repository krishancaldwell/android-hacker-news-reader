package me.kcaldwell.hackernewsreader.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.adapters.ArticleRecyclerViewAdapter;
import me.kcaldwell.hackernewsreader.api.News;
import me.kcaldwell.hackernewsreader.data.FeedItem;
import me.kcaldwell.hackernewsreader.dummy.DummyContent;
import me.kcaldwell.hackernewsreader.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnArticleSelectedListener}
 * interface.
 */
public class ArticleListFragment extends Fragment {

    private static final String TAG = ArticleListFragment.class.getSimpleName();

    private Realm mRealm;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnArticleSelectedListener mListener;

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
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_list, container, false);

        mRealm = Realm.getDefaultInstance();

        // Set the adapter
        if (rootView instanceof RecyclerView) {
            Context context = rootView.getContext();
            RecyclerView recyclerView = (RecyclerView) rootView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ArticleRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }

        getArticles();

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnArticleSelectedListener) {
            mListener = (OnArticleSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        News.get(getActivity(), response -> {
            final FeedItem feedItem = new FeedItem();
            mRealm.executeTransaction(realm -> {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject article = response.getJSONObject(i);
                        feedItem.setId(article.getLong("id"));
                        feedItem.setTitle(article.getString("title"));
                        feedItem.setPoints(article.getInt("points"));
                        feedItem.setAuthor(article.getString("user"));
                        feedItem.setTime(article.getLong("time"));
                        feedItem.setTimeAgo(article.getString("time_ago"));
                        feedItem.setCommentsCount(article.getInt("comments_count"));
                        feedItem.setType(article.getString("type"));
                        feedItem.setUrl(article.getString("url"));
                        feedItem.setDomain(article.getString("domain"));
                        mRealm.insert(feedItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }, () -> {
            Log.e(TAG, "An error occurred");

            Toast.makeText(getActivity(), "There was an error updating the stories", Toast.LENGTH_LONG).show();
        });
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
        void onArticleSelected(DummyItem item);
    }
}
