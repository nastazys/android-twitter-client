package com.example.twitty.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.twitty.R;
import com.example.twitty.adapter.TweetAdapter;
import com.twitter.sdk.android.core.Twitter;

abstract class TimelineActivity extends AppCompatActivity {
    protected RecyclerView tweetsRecyclerView;
    protected TweetAdapter tweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);

        initToolbar();

        tweetsRecyclerView = findViewById(R.id.tweets_recycler_view);
        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetAdapter = new TweetAdapter(TimelineActivity.this);
        tweetsRecyclerView.setAdapter(tweetAdapter);
    }

    void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    abstract void loadTweets();
}
