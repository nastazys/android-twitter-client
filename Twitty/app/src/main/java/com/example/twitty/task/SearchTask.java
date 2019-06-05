package com.example.twitty.task;

import android.annotation.SuppressLint;
import android.widget.EditText;

import com.example.twitty.adapter.TweetAdapter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.services.SearchService;

import retrofit2.Call;

public class SearchTask extends TimelineTask {
    @SuppressLint("StaticFieldLeak")
    private EditText searchText;

    public SearchTask(TweetAdapter adapter, EditText searchText){
        super(adapter);
        this.searchText = searchText;
    }

    @Override
    protected Void doInBackground(Void... temp) {
        String request = searchText.getText().toString();
        final SearchService searchService = TwitterCore.getInstance().getApiClient().getSearchService();
        Call<Search> list = searchService.tweets
                (request, null, null, null, null, 10000, null, null, null, false);
        list.enqueue(new Callback<Search>() {
            @Override
            public void success(Result<Search> result) {
                tweetAdapter.clearItems();
                tweetAdapter.setItems(result.data.tweets);
            }

            @Override
            public void failure(TwitterException exception) {
            }
        });
        return null;
    }
}
