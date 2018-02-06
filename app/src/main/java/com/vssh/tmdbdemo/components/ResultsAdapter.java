package com.vssh.tmdbdemo.components;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vssh.tmdbdemo.R;
import com.vssh.tmdbdemo.connector.TmdbApi;
import com.vssh.tmdbdemo.databinding.ItemListContentBinding;
import com.vssh.tmdbdemo.models.GenericItem;
import com.vssh.tmdbdemo.models.MovieItem;
import com.vssh.tmdbdemo.models.TvItem;

import java.util.List;

/**
 * Recycler adapter for the search results.
 */

public class ResultsAdapter
        extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    private Context context;
    private List<? extends GenericItem> mValues;
    private View.OnClickListener itemListener;

    public ResultsAdapter(Context context, List<? extends GenericItem> items, View.OnClickListener itemListener) {
        mValues = items;
        this.context = context;
        this.itemListener = itemListener;
    }

    public void setData(final List<? extends GenericItem> items) {
        if(mValues == null) {
            mValues = items;
            notifyDataSetChanged();
        }
        else {
            // use DiffUtils to update only the changed items to the adapter
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mValues.size();
                }

                @Override
                public int getNewListSize() {
                    return items.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mValues.get(oldItemPosition).getId() == items.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return areItemsTheSame(oldItemPosition, newItemPosition);
                }
            });
            mValues = items;
            result.dispatchUpdatesTo(this);
        }
    }

    /**
     * Use Glide to load image directly from url to the imageView
     * @param url URL to load
     * @param view ImageView to load into
     */
    private void loadPosterImage(String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.fitCenterTransform())
                .into(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Data binding for the ViewHolder
        ItemListContentBinding contentBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_list_content, parent, false);
        return new ViewHolder(contentBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GenericItem item = mValues.get(position);
        if(mValues.get(position) instanceof MovieItem) {
            MovieItem movieItem = (MovieItem) mValues.get(position);
            holder.binding.title.setText(movieItem.getTitle());
        }
        else if(mValues.get(position) instanceof TvItem) {
            TvItem tvItem = (TvItem) mValues.get(position);
            holder.binding.title.setText(tvItem.getName());
        }

        if(item.getPoster_path() != null && !"".equals(item.getPoster_path())) {
            loadPosterImage(TmdbApi.IMAGE_POSTER_SMALL_URL+item.getPoster_path(), holder.binding.poster);
        }

        holder.binding.rating.setRating((float) item.getVote_average());
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(itemListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemListContentBinding binding;

        ViewHolder(ItemListContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}