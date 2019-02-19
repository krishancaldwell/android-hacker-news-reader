package me.kcaldwell.hackernewsreader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.kevinsawicki.timeago.TimeAgo;

import java.util.List;

import me.kcaldwell.hackernewsreader.ui.ArticleListFragment.OnArticleSelectedListener;
import me.kcaldwell.hackernewsreader.R;
import me.kcaldwell.hackernewsreader.dummy.DummyContent.DummyItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnArticleSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mArticles;
    private final OnArticleSelectedListener mListener;

    public ArticleRecyclerViewAdapter(List<DummyItem> articles, OnArticleSelectedListener listener) {
        mArticles = articles;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DummyItem article = mArticles.get(position);
        holder.mItem = article;
        holder.mTitleTextView.setText(article.title);
        holder.mPreviewView.setText(article.content);
        holder.mAuthorTextView.setText(article.by);

        // Calculate time in human readable form
        TimeAgo timeUtil = new TimeAgo();
        long currentTime = System.currentTimeMillis();
        String time = timeUtil.time(currentTime - article.time, false);
        holder.mTimePostedTextView.setText(time);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onArticleSelected(holder.mItem);
            }
        });

        if (position == getItemCount() - 1) holder.mDivider.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleTextView;
        public final TextView mPreviewView;
        public final TextView mAuthorTextView;
        public final TextView mTimePostedTextView;
        public final ImageButton mBookmarkButton;
        public final ImageButton mCommentButton;
        public final View mDivider;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleTextView = view.findViewById(R.id.title_text_view);
            mPreviewView = view.findViewById(R.id.preview_text_view);
            mAuthorTextView = view.findViewById(R.id.author_text_view);
            mTimePostedTextView = view.findViewById(R.id.time_text_view);
            mBookmarkButton = view.findViewById(R.id.bookmark_button);
            mCommentButton = view.findViewById(R.id.comment_button);
            mDivider = view.findViewById(R.id.article_divider);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPreviewView.getText() + "'";
        }
    }
}
