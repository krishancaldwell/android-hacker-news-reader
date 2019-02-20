package me.kcaldwell.hackernewsreader.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.adapters.CommentRecyclerViewAdapter;
import me.kcaldwell.hackernewsreader.api.Item;
import me.kcaldwell.hackernewsreader.data.Comment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CommentListFragment extends Fragment {

    public static final String TAG = CommentListFragment.class.getSimpleName();

    private Realm mRealm;
    private String mArticleId;

    private RecyclerView mRecyclerView;
    private CommentRecyclerViewAdapter mAdapter;

    private OnListFragmentInteractionListener mListener;

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

        mRecyclerView = rootView.findViewById(R.id.list);

        // Set the adapter
        RealmResults<Comment> parentComments = mRealm.where(Comment.class)
                .sort("level", Sort.ASCENDING)
                .findAll();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        mAdapter = new CommentRecyclerViewAdapter(parentComments, mListener);
        mRecyclerView.setAdapter(mAdapter);

        getComments();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRealm != null) {
            mRealm.close();
        }
    }

    private void getComments() {
        RealmResults<Comment> oldComments = mRealm.where(Comment.class).findAll();
        mRealm.executeTransaction(realm -> oldComments.deleteAllFromRealm());
        Item.get(getActivity(), mArticleId, (response) -> {
                    mRealm.executeTransaction(realm -> {
                        JSONArray comments;
                        try {
                            comments = response.getJSONArray("comments");
                            addCommentToRealm(realm, comments, null);
                        }
                        catch (JSONException e) {
                            Log.e(TAG, "JSON Exception: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                },
                () -> Log.e(TAG, "An error occurred with the API call"));
    }

    private void addCommentToRealm(Realm realm, JSONArray comments, @Nullable Long parentId) {
        final Comment comment = new Comment();
        for (int i = 0; i < comments.length(); i++) {
            try {
                JSONObject jsonObject = comments.getJSONObject(i);

                comment.setId(jsonObject.getLong("id"));
                if(jsonObject.has("user")) {
                    comment.setAuthor(jsonObject.getString("user"));
                }
                else {
                    comment.setAuthor("Deleted User");
                }
                comment.setTime(jsonObject.getLong("time"));
                comment.setTimeAgo(jsonObject.getString("time_ago"));
                comment.setContent(jsonObject.getString("content"));
                comment.setType(jsonObject.getString("type"));
                comment.setUrl(jsonObject.getString("url"));
                comment.setCommentsCount(jsonObject.getInt("comments_count"));
                if (jsonObject.has("level")) {
                    comment.setLevel(jsonObject.getInt("level"));
                }
                realm.copyToRealmOrUpdate(comment);

                if (parentId != null) {
                    Comment parent = realm.where(Comment.class).equalTo("id", parentId).findFirst();
                    if (parent == null) {
                        return;
                    }

                    parent.getComments().add(comment);
                    realm.copyToRealmOrUpdate(parent);
                }

                JSONArray childComments = jsonObject.getJSONArray("comments");
                if (comments.length() > 0) {
                    addCommentToRealm(realm, childComments, comment.getId());
                }
            }
            catch (JSONException e) {
                Log.e(TAG, "JSON Exception: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Comment comment);
    }
}
