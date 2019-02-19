package me.kcaldwell.hackernewsreader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.data.FeedItem;
import me.kcaldwell.hackernewsreader.ui.ArticleListFragment.OnArticleSelectedListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FeedItem} and makes a call to the
 * specified {@link OnArticleSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ArticleRecyclerViewAdapter extends RealmRecyclerViewAdapter<FeedItem, ArticleRecyclerViewAdapter.ViewHolder> {

    private final OnArticleSelectedListener mListener;

    public ArticleRecyclerViewAdapter(OrderedRealmCollection<FeedItem> data, OnArticleSelectedListener listener) {
        super(data, true);
        mListener = listener;

        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FeedItem item = getItem(position);
        holder.mFeedItem = item;
        holder.mTitleTextView.setText(item.getTitle());
        if (item.getDomain() == null || item.getDomain().equals("null")) {
            holder.mDomainTextView.setText("");
        }
        else {
            holder.mDomainTextView.setText(item.getDomain());
        }
        holder.mAuthorTextView.setText(item.getAuthor());
        holder.mTimePostedTextView.setText(item.getTimeAgo());

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onArticleSelected(holder.mFeedItem);
            }
        });

        if (position == getItemCount() - 1) holder.mDivider.setVisibility(View.INVISIBLE);
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleTextView;
        public final TextView mDomainTextView;
        public final TextView mAuthorTextView;
        public final TextView mTimePostedTextView;
        public final ImageButton mBookmarkButton;
        public final ImageButton mCommentButton;
        public final View mDivider;
        public FeedItem mFeedItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleTextView = view.findViewById(R.id.title_text_view);
            mDomainTextView = view.findViewById(R.id.domain_text_view);
            mAuthorTextView = view.findViewById(R.id.author_text_view);
            mTimePostedTextView = view.findViewById(R.id.time_text_view);
            mBookmarkButton = view.findViewById(R.id.bookmark_button);
            mCommentButton = view.findViewById(R.id.comment_button);
            mDivider = view.findViewById(R.id.article_divider);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDomainTextView.getText() + "'";
        }
    }
}
