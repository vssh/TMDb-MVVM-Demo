package com.vssh.tmdbdemo.ui;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.database.MatrixCursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.vssh.tmdbdemo.components.FixedSizeQueue;
import com.vssh.tmdbdemo.R;
import com.vssh.tmdbdemo.components.ResultsAdapter;
import com.vssh.tmdbdemo.databinding.ItemListBinding;
import com.vssh.tmdbdemo.models.GenericItem;

import java.util.List;

/**
 * Fragment showing the search bar and the results list.
 */

public class ItemListFragment extends Fragment {
    private ItemListBinding binding;
    private MainViewModel viewModel;
    private ResultsAdapter resultsAdapter;
    private Context context;
    SimpleCursorAdapter suggestionsAdapter;
    AlertDialog errorDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Data binding for the fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.item_list, null, false);
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

        viewModel = ((MainActivity) getActivity()).getViewModel();

        setupRecyclerView(binding.itemList);

        // Observe changes in search results and notify adapter
        viewModel.searchItems.observe(this, new Observer<List<? extends GenericItem>>() {
            @Override
            public void onChanged(@Nullable List<? extends GenericItem> items) {
                resultsAdapter.setData(items);
                resultsAdapter.notifyDataSetChanged();
            }
        });

        // set up search suggestions
        MatrixCursor cursor = viewModel.getSuggestionCursor();
        String[] from = {"text"};
        int[] to = {android.R.id.text1};
        suggestionsAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_dropdown_item_1line, cursor, from, to, 0);
        binding.searchView.setSuggestionsAdapter(suggestionsAdapter);
        cursor.close();

        binding.searchView.setIconified(false);
        binding.searchView.setIconifiedByDefault(false);

        // allow to show suggestion list even when empty
        AutoCompleteTextView searchText = binding.searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchText.setThreshold(0);
        binding.searchView.clearFocus();

        // run search query on submit
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.onSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // set query on suggestion click
        binding.searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                int size = viewModel.suggestionsList.getValue().size();
                if (size > position) {
                    binding.searchView.setQuery(viewModel.suggestionsList.getValue().get(size-position-1), true);
                }
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                int size = viewModel.suggestionsList.getValue().size();
                if (size > position) {
                    binding.searchView.setQuery(viewModel.suggestionsList.getValue().get(size-position-1), true);
                }
                return true;
            }
        });

        // observe change in suggestions and load
        viewModel.suggestionsList.observe(this, new Observer<FixedSizeQueue<String>>() {
            @Override
            public void onChanged(@Nullable FixedSizeQueue<String> strings) {
                suggestionsAdapter.changeCursor(viewModel.getSuggestionCursor());
            }
        });

        // change image on TV toggle
        binding.tvToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                viewModel.onTvToggle(b);
                if(b) {
                    binding.tvToggle.setButtonDrawable(getResources().getDrawable(R.drawable.ic_tv_24dp));
                }
                else {
                    binding.tvToggle.setButtonDrawable(getResources().getDrawable(R.drawable.ic_local_movies_24dp));
                }
            }
        });
        binding.tvToggle.setChecked(viewModel.isTv.getValue());
        binding.tvToggle.setBackground(null);
        binding.tvToggle.setButtonDrawable(getResources().getDrawable(R.drawable.ic_local_movies_24dp));

        // load detailsFragment when an item is selected
        viewModel.selectedItem.observe(getActivity(), new Observer<GenericItem>() {
            @Override
            public void onChanged(@Nullable GenericItem genericItem) {
                if(genericItem != null) {
                    if (getActivity() != null) {
                        ((MainActivity) getActivity()).loadDetailsFragment();
                    }
                }
            }
        });

        // Show errors when available
        viewModel.errorMsg.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null && !"".equals(s)) {
                    String msg = s;
                    if(msg.equals(MainViewModel.ERROR_MSG_NO_RESULTS)) {
                        msg = context.getResources().getString(R.string.no_results);
                    }
                    viewModel.errorMsg.setValue(null);
                    errorDialog = new AlertDialog.Builder(context)
                            .setMessage(msg)
                            .setTitle(R.string.error_encountered)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //do nothing
                                }
                            })
                            .create();
                    errorDialog.show();
                }
            }
        });

        // show and hide the loading view
        viewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean) {
                    binding.tvToggle.setVisibility(View.GONE);
                    binding.loading.setVisibility(View.VISIBLE);
                }
                else {
                    binding.tvToggle.setVisibility(View.VISIBLE);
                    binding.loading.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Set up the recyclerView
     * @param recyclerView RecyclerView
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        resultsAdapter = new ResultsAdapter(context, viewModel.searchItems.getValue(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenericItem item = (GenericItem) view.getTag();
                viewModel.onItemSelected(item);
            }
        });
        recyclerView.setAdapter(resultsAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        if(errorDialog != null) {
            errorDialog.dismiss();
            errorDialog = null;
        }
    }
}
