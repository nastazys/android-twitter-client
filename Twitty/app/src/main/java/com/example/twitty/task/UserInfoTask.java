package com.example.twitty.task;

import android.widget.Toast;

import com.example.twitty.activity.UserInfoActivity;
import com.example.twitty.adapter.TweetAdapter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import retrofit2.Call;

public class UserInfoTask extends TimelineTask {
    private Long userId;
    private User user;
    private UserInfoActivity context;

    public UserInfoTask(TweetAdapter adapter, Long userId, UserInfoActivity activity) {
        super(adapter);
        this.userId = userId;
        context = activity;
    }

    @Override
    protected Void doInBackground(Void... temp) {
        final StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();
        Call<List<Tweet>> list = statusesService
                .userTimeline(userId, null, 800, null, null, false, false, false, null);
        list.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                tweetAdapter.setItems(result.data);
                context.displayUserInfo(result.data.get(0).user);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(context, "Отсутствует соединение!", Toast.LENGTH_LONG).show();
            }
        });
        return null;
    }
}
