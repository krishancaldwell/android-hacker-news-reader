package me.kcaldwell.hackernewsreader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.data.Item;
import me.kcaldwell.hackernewsreader.ui.CommentFragment.OnListFragmentInteractionListener;
import me.kcaldwell.hackernewsreader.ui.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CommentRecyclerViewAdapter extends RealmRecyclerViewAdapter<Item, CommentRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;

    public CommentRecyclerViewAdapter(OrderedRealmCollection<Item> data, OnListFragmentInteractionListener listener) {
        super(data, true);
        mListener = listener;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item item = getItem(position);
        holder.mItem = item;
        holder.mAuthorTextView.setText(item.getAuthor());
        holder.mKarmaTextView.setText(item.getPoints());
        holder.mContentView.setText(item.getContent());
        holder.mTimeTextView.setText(item.getTimeAgo());
        int commentsCount = item.getCommentsCount();
        String commentsString = commentsCount > 1 ? commentsCount + " replies" : " reply";
        holder.mRepliesTextView.setText(commentsString);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });

        if (position == getItemCount() - 1) holder.mDivider.setVisibility(View.INVISIBLE);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAuthorTextView;
        public final TextView mKarmaTextView;
        public final TextView mContentView;
        public final TextView mTimeTextView;
        public final TextView mRepliesTextView;
        public final View mCommentSpine;
        public final View mDivider;
        public Item mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAuthorTextView = view.findViewById(R.id.author_text_view);
            mKarmaTextView = view.findViewById(R.id.karma_text_view);
            mContentView = view.findViewById(R.id.content_text_view);
            mTimeTextView = view.findViewById(R.id.time_text_view);
            mRepliesTextView = view.findViewById(R.id.reply_count_text_view);
            mCommentSpine = view.findViewById(R.id.comment_spine);
            mDivider = view.findViewById(R.id.comment_divider);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
