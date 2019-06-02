package com.example.twitty.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.twitty.R;
import com.example.twitty.task.SearchTask;

public class SearchActivity extends TimelineActivity {
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
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
                task = new SearchTask(tweetAdapter, searchText);
                loadTweets();
            }
        });
    }

    @Override
    void initTask() {
        task = new SearchTask(tweetAdapter, searchText);
    }


}
