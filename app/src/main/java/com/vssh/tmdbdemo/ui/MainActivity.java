package com.vssh.tmdbdemo.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;

import com.vssh.tmdbdemo.R;
import com.vssh.tmdbdemo.models.GenericItem;

import java.util.ArrayList;


/**
 * Main Activity. Manages the loading of the 2 fragments.
 */
public class MainActivity extends AppCompatActivity {
    private ItemListFragment itemListFragment;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate the listFragment
        itemListFragment = new ItemListFragment();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideLeft = new Slide(GravityCompat.getAbsoluteGravity(GravityCompat.START, getResources().getConfiguration().getLayoutDirection()));
            itemListFragment.setEnterTransition(slideLeft);
        }
        loadListFragment();

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // pass the sahredPreferences to the ViewModel
        viewModel.setPreferences(preferences);
    }

    public MainViewModel getViewModel() {
        return viewModel;
    }

    /**
     * Load the detailsFragment instead of listFragment
     */
    public void loadDetailsFragment() {
        ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideRight = new Slide(GravityCompat.getAbsoluteGravity(GravityCompat.END, getResources().getConfiguration().getLayoutDirection()));
            itemDetailFragment.setEnterTransition(slideRight);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, itemDetailFragment);
        transaction.commit();
    }

    /**
     * Load the listFragment
     */
    public void loadListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, itemListFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(viewModel.selectedItem.getValue() != null) {
            // load the listFragment again if detailsFragment was shown
            viewModel.selectedItem.setValue(null);
            loadListFragment();
        }
        else if(viewModel.searchItems.getValue().size() >0) {
            // clear the list
            viewModel.searchItems.setValue(new ArrayList<GenericItem>());
        }
        else {
            // go to default action (mostly just exit the app)
            super.onBackPressed();
        }
    }
}
