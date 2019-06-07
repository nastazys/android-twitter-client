package com.example.twitty.task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.twitty.activity.MainActivity;
import com.example.twitty.adapter.TweetAdapter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import retrofit2.Call;

public class TimelineTask extends AsyncTask<Void, Void, Void> {
    TweetAdapter tweetAdapter;
    private MainActivity context;

    public TimelineTask(TweetAdapter adapter, MainActivity context) {
        tweetAdapter = adapter;
        this.context = context;
    }

    public TimelineTask(TweetAdapter adapter) {
        tweetAdapter = adapter;
    }

    @Override
    protected Void doInBackground(Void... temp) {
        final StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();
        Call<List<Tweet>> list = statusesService
                .homeTimeline(800, null, null, false, false, false, null);
        list.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                tweetAdapter.clearItems();
                tweetAdapter.setItems(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(context, "Отсутствует соединение!", Toast.LENGTH_LONG).show();
            }
        });
        return null;
    }
}