package com.example.twitty.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.example.twitty.R;
import com.example.twitty.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.core.TwitterCore;

import com.twitter.sdk.android.core.TwitterSession;

abstract class StandardTimelineActivity extends TimelineActivity implements SwipeRefreshLayout.OnRefreshListener{
    protected SwipeRefreshLayout swipeHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTweets();

        swipeHome = (SwipeRefreshLayout) findViewById(R.id.swipe_home);
        swipeHome.setOnRefreshListener(this);
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
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.action_add_tweet) {
            final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                    .getActiveSession();
            final Intent intent = new ComposerActivity.Builder(StandardTimelineActivity.this)
                    .session(session)
                    .darkTheme()
                    .createIntent();
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initTask();
                loadTweets();
                swipeHome.setRefreshing(false);
            }
        }, 800);
    }
}