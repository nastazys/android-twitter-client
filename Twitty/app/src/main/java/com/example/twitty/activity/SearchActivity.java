package com.example.twitty.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.example.twitty.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.services.SearchService;

import retrofit2.Call;

public class SearchActivity extends TimelineActivity {
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
    }

    void loadTweets() {
        String request = searchText.getText().toString();
        final SearchService searchService = TwitterCore.getInstance().getApiClient().getSearchService();
        Call<Search> list = searchService.tweets
                (request, null, null, null, null, 800, null, null, null, false);
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
    }

    @Override
    void initToolbar() {
        super.initToolbar();
        searchText = findViewById(R.id.request_edit_text);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadTweets();
            }
        });
    }


}
