package com.vssh.tmdbdemo.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.util.Log;

import com.vssh.tmdbdemo.components.FixedSizeQueue;
import com.vssh.tmdbdemo.connector.TmdbApi;
import com.vssh.tmdbdemo.models.GenericItem;
import com.vssh.tmdbdemo.models.ItemList;
import com.vssh.tmdbdemo.models.MovieItem;
import com.vssh.tmdbdemo.models.TvItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ViewModel for the app
 */

public class MainViewModel extends ViewModel {
    private static final String PREFERENCE_KEY_SUGGESTIONS = "suggestions";
    static final String ERROR_MSG_NO_RESULTS = "no_results";

    // create all LiveData instances needed
    MutableLiveData<List<? extends GenericItem>> searchItems = new MutableLiveData<>();
    MutableLiveData<GenericItem> selectedItem = new MutableLiveData<>();
    MutableLiveData<Boolean> isTv = new MutableLiveData<>();
    MutableLiveData<FixedSizeQueue<String>> suggestionsList = new MutableLiveData<>();
    MutableLiveData<String> errorMsg = new MutableLiveData<>();
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private SharedPreferences preferences;

    private TmdbApi api;

    public MainViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TmdbApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(TmdbApi.class);
        isTv.setValue(false);
        selectedItem.setValue(null);
        searchItems.setValue(new ArrayList<GenericItem>());

        FixedSizeQueue<String> sugg = new FixedSizeQueue<String>(10);
        suggestionsList.setValue(sugg);
        errorMsg.setValue(null);
        isLoading.setValue(false);
    }

    /**
     * Receive SharedPreferences
     * @param preferences {SharedPreferences}
     */
    void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;

        // load suggestions list
        String suggestionsString = preferences.getString(PREFERENCE_KEY_SUGGESTIONS, "");
        String[] splitSuggestions = suggestionsString.split("\\|");
        FixedSizeQueue<String> queue = new FixedSizeQueue(10);
        for(String s : splitSuggestions) {
            if (!s.isEmpty()) {
                queue.add(s);
            }
        }
        suggestionsList.setValue(queue);
    }

    /**
     * set state on TV toggle
     * @param val isTv
     */
    void onTvToggle(boolean val) {
        isTv.setValue(val);
    }

    /**
     * send the query for TV or movie
     * @param query query to send
     */
    void onSearchQuery(final String query) {
        if("".equals(TmdbApi.API_KEY)) {
            Log.e("MainViewModel", "TMDb API key not set. Check your gradle.properties");
            return;
        }

        if(isTv.getValue()) {
            // get call fro Retrofit interface
            Call<ItemList<TvItem>> call = api.getTvSearchItems(query);
            isLoading.setValue(true);
            // enqueue call and act on callback
            call.enqueue(new Callback<ItemList<TvItem>>() {
                @Override
                public void onResponse(Call<ItemList<TvItem>> call, Response<ItemList<TvItem>> response) {
                    isLoading.setValue(false);
                    List<TvItem> items = response.body().getResults();
                    if (items != null) {
                        // set the search results on success
                        searchItems.setValue(items);
                    } else {
                        searchItems.setValue(new ArrayList<GenericItem>());
                    }
                    // set error to show if no results found
                    if(searchItems.getValue().size() > 0) {
                        FixedSizeQueue<String> tempList = suggestionsList.getValue();
                        tempList.add(query);
                        suggestionsList.setValue(tempList);
                        saveSuggestions();
                    }
                    else {
                        errorMsg.setValue(ERROR_MSG_NO_RESULTS);
                    }
                }

                @Override
                public void onFailure(Call<ItemList<TvItem>> call, Throwable t) {
                    isLoading.setValue(false);
                    errorMsg.setValue(t.getMessage());
                }
            });
        }
        else {
            Call<ItemList<MovieItem>> call = api.getMovieSearchItems(query);
            isLoading.setValue(true);
            call.enqueue(new Callback<ItemList<MovieItem>>() {
                @Override
                public void onResponse(Call<ItemList<MovieItem>> call, Response<ItemList<MovieItem>> response) {
                    isLoading.setValue(false);
                    List<MovieItem> items = response.body().getResults();
                    if (items != null) {
                        searchItems.setValue(items);
                    } else {
                        searchItems.setValue(new ArrayList<GenericItem>());
                    }
                    if(searchItems.getValue().size() > 0) {
                        FixedSizeQueue<String> tempList = suggestionsList.getValue();
                        tempList.add(query);
                        suggestionsList.setValue(tempList);
                        saveSuggestions();
                    }
                    else {
                        errorMsg.setValue(ERROR_MSG_NO_RESULTS);
                    }
                }

                @Override
                public void onFailure(Call<ItemList<MovieItem>> call, Throwable t) {
                    isLoading.setValue(false);
                    errorMsg.setValue(t.getMessage());
                }
            });
        }
    }

    /**
     * save selection on item click
     * @param item item so save to state
     */
    void onItemSelected(GenericItem item) {
        selectedItem.setValue(item);
    }

    /**
     * save suggestions list to SharedPreferences
     */
    private void saveSuggestions() {
        FixedSizeQueue<String> queue = suggestionsList.getValue();
        StringBuilder builder = new StringBuilder();
        for(String string : queue) {
            builder.append(string).append("|");
        }
        String suggestionsString = builder.toString();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFERENCE_KEY_SUGGESTIONS, suggestionsString);
        editor.apply();
    }

    /**
     * Retrieve suggestions as cursor
     * @return suggestions cursor
     */
    MatrixCursor getSuggestionCursor() {
        String[] columnNames = {"_id","text"};
        MatrixCursor cursor = new MatrixCursor(columnNames);
        String[] array = suggestionsList.getValue().toArray(new String[suggestionsList.getValue().size()]);
        String[] temp = new String[2];
        int id = 0;
        for(int i=array.length-1; i>=0; i--){
            temp[0] = Integer.toString(id++);
            temp[1] = array[i];
            cursor.addRow(temp);
        }
        return cursor;
    }
}
