package com.vssh.tmdbdemo.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vssh.tmdbdemo.R;
import com.vssh.tmdbdemo.connector.TmdbApi;
import com.vssh.tmdbdemo.databinding.ItemDetailBinding;
import com.vssh.tmdbdemo.models.GenericItem;
import com.vssh.tmdbdemo.models.MovieItem;
import com.vssh.tmdbdemo.models.TvItem;

/**
 * A fragment representing a single Item detail screen.
 */
public class ItemDetailFragment extends Fragment {
    private ItemDetailBinding binding;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Data binding for the fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.item_detail, null, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainViewModel viewModel = ((MainActivity) getActivity()).getViewModel();

        // get the selected item
        GenericItem item = viewModel.selectedItem.getValue();

        String title = "";
        if(item instanceof MovieItem) {
            title = ((MovieItem) item).getTitle();
        }
        else if(item instanceof TvItem) {
            title = ((TvItem) item).getName();
        }

        binding.title.setText(title);
        binding.rating.setRating((float) (item.getVote_average()));
        binding.votes.setText(getResources().getString(R.string.in_parentheses, item.getVote_count()));
        binding.overview.setText(item.getOverview());

        // load poster
        if (item.getPoster_path() != null && !"".equals(item.getPoster_path())) {
            Glide.with(context)
                    .load(TmdbApi.IMAGE_POSTER_LARGE_URL+item.getPoster_path())
                    .apply(RequestOptions.fitCenterTransform())
                    .into(binding.poster);
        }

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        //load backdrop
        if(item.getBackdrop_path() != null && !"".equals(item.getBackdrop_path())) {
            Glide.with(context)
                    .load(TmdbApi.IMAGE_BACKDROP_URL+item.getBackdrop_path())
                    .apply(RequestOptions.centerCropTransform())
                    .into(binding.backdrop);
        }
    }
}
