package me.kcaldwell.hackernewsreader.adapters;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.data.FeedItem;
import me.kcaldwell.hackernewsreader.ui.ArticleListFragment;
import me.kcaldwell.hackernewsreader.ui.ArticleListFragment.OnArticleSelectedListener;
import me.kcaldwell.hackernewsreader.ui.ArticleListFragment.OnArticleCommentsSelectedListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FeedItem} and makes a call to the
 * specified {@link OnArticleSelectedListener}.
 */
public class ArticleRecyclerViewAdapter extends RealmRecyclerViewAdapter<FeedItem, ArticleRecyclerViewAdapter.ViewHolder> {

    private final OnArticleSelectedListener mArticleSelectedListener;
    private final OnArticleCommentsSelectedListener mCommentsListener;

    public ArticleRecyclerViewAdapter(OrderedRealmCollection<FeedItem> data, OnArticleSelectedListener listener, ArticleListFragment.OnArticleCommentsSelectedListener commentsListener) {
        super(data, true);
        mArticleSelectedListener = listener;
        mCommentsListener = commentsListener;
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
        if (item.getPoints() == null || item.getPoints().equals("null")) {
            holder.mPointsTextView.setText("");
        }
        else {
            holder.mPointsTextView.setText("+" + item.getPoints());
        }
        holder.mCommentCount.setText(String.valueOf(item.getCommentsCount()));
        holder.mTimePostedTextView.setText(item.getTimeAgo());

        holder.mView.setOnClickListener(v -> {
            if (null != mArticleSelectedListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mArticleSelectedListener.onArticleSelected(holder.mFeedItem);
            }
        });

        holder.mCommentHolder.setOnClickListener(v -> {
            // Send data back
            mCommentsListener.onCommentsSelected(holder.mFeedItem);
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
        public final TextView mPointsTextView;
        public final ConstraintLayout mCommentHolder;
        public final TextView mCommentCount;
        public final View mDivider;
        public FeedItem mFeedItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleTextView = view.findViewById(R.id.title_text_view);
            mDomainTextView = view.findViewById(R.id.domain_text_view);
            mAuthorTextView = view.findViewById(R.id.author_text_view);
            mTimePostedTextView = view.findViewById(R.id.time_text_view);
            mPointsTextView = view.findViewById(R.id.karma_text_view);
            mCommentHolder = view.findViewById(R.id.comment_holder);
            mCommentCount = view.findViewById(R.id.comment_count_text_view);
            mDivider = view.findViewById(R.id.article_divider);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDomainTextView.getText() + "'";
        }
    }
}
