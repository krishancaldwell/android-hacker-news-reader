package me.kcaldwell.hackernewsreader.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.adapters.CommentRecyclerViewAdapter;
import me.kcaldwell.hackernewsreader.api.Item;
import me.kcaldwell.hackernewsreader.data.Comment;
import me.kcaldwell.hackernewsreader.data.CommentDao;

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the {@link OnCommentInteractionListener}
 * interface.
 */
public class CommentListFragment extends Fragment {

    private static final String TAG = CommentListFragment.class.getSimpleName();

    private Realm mRealm;
    private String mArticleId;
    private RealmResults<Comment> mComments;
    private Parcelable mRecyclerViewState;

    private TextView mCommentCountTextView;
    private RecyclerView mRecyclerView;
    private CommentRecyclerViewAdapter mAdapter;
    private View mLoadingView;
    private LottieAnimationView mAnimationView;

    private OnCommentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommentListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mArticleId = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comment_list, container, false);

        mRealm = Realm.getDefaultInstance();

        mCommentCountTextView = rootView.findViewById(R.id.comment_count_text_view);
        mRecyclerView = rootView.findViewById(R.id.list);
        mLoadingView = rootView.findViewById(R.id.loading_mask);
        mAnimationView = rootView.findViewById(R.id.animation_view);

        // Set the adapter
        mComments = CommentDao.getAllComments(mRealm);
        int commentsCount = mComments.size();
        String commentsString;
        if (commentsCount == 0) {
            commentsString = "No comments";
        } else if (commentsCount == 1) {
            commentsString = "1 comment";
        } else {
            commentsString = commentsCount + " comments";
        }
        mCommentCountTextView.setText(commentsString);

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new CommentRecyclerViewAdapter(mComments, mListener);
        mRecyclerView.setAdapter(mAdapter);

        getComments();

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Save the state of the recycler view so the scroll position can be preserved
        mRecyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRealm != null) {
            mRealm.close();
        }
    }

    private void toggleProgressViews(boolean show) {
        int visible = show ? View.VISIBLE : View.GONE;
        mLoadingView.setVisibility(visible);
        mAnimationView.setVisibility(visible);
    }

    private void getComments() {
        toggleProgressViews(true);
        CommentDao.removeAllComments(mRealm);
        Item.get(getActivity(), mArticleId, (response) -> mRealm.executeTransaction(realm -> {
                    JSONArray comments;
                    try {
                        comments = response.getJSONArray("comments");
                        CommentDao.createCommentsFromArray(comments, realm, null);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Exception: " + e.getMessage());
                        e.printStackTrace();
                    }

                    refreshAdapterArticles();
                    scrollToPositionIfSet();
                    toggleProgressViews(false);
                }),
                () -> Log.e(TAG, "An error occurred with the API call"));
    }

    private void refreshAdapterArticles() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void scrollToPositionIfSet() {
        if (mRecyclerViewState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerViewState);
        } else {
            Log.i(TAG, "State was null");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCommentInteractionListener) {
            mListener = (OnCommentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCommentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCommentInteractionListener {
        void onCommentUrlClicked(String url);
    }
}
