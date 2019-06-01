package com.example.twitty.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.twitty.R;
import com.example.twitty.adapter.TweetAdapter;
import com.example.twitty.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;

import com.twitter.sdk.android.core.TwitterSession;

abstract class TweetTimelineActivity extends AppCompatActivity {
    protected RecyclerView tweetsRecyclerView;
    protected TweetAdapter tweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tweetsRecyclerView = findViewById(R.id.tweets_recycler_view);
        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetAdapter = new TweetAdapter(TweetTimelineActivity.this);
        tweetsRecyclerView.setAdapter(tweetAdapter);

        loadTweets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.action_add_tweet) {
            final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                    .getActiveSession();
            final Intent intent = new ComposerActivity.Builder(TweetTimelineActivity.this)
                    .session(session)
                    .darkTheme()
                    .createIntent();
            startActivity(intent);
        }

        return true;
    }

    abstract void loadTweets();
}