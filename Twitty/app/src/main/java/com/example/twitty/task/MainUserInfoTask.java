package com.example.twitty.task;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twitty.R;
import com.example.twitty.activity.LoginActivity;
import com.example.twitty.activity.UserInfoActivity;
import com.example.twitty.pojo.SimpleUser;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;

public class MainUserInfoTask extends AsyncTask<TwitterSession, Integer, SimpleUser> {
    private SimpleUser user;
    private Activity currentActivity;

    public MainUserInfoTask(Activity activity){
        currentActivity = activity;
    }

    @Override
    protected SimpleUser doInBackground(TwitterSession[] session) {
        TwitterApiClient apiClient = new TwitterApiClient(session[0]);
        Call<User> userResult = apiClient.getAccountService()
                .verifyCredentials(true, false, false);
        userResult.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                //user = new SimpleUser (result.data);
            }

            @Override
            public void failure(TwitterException exception) {
            }
        });
        return user;
    }

    protected void onPostExecute() {
        Intent intent = new Intent(currentActivity, UserInfoActivity.class);
        intent.putExtra("userName", user.getName());
        intent.putExtra("id", user.getId());
        intent.putExtra("profileImageUrl", user.getImageUrl());
        intent.putExtra("description", user.getDescription());
        intent.putExtra("followingCount", user.getFollowingNum());
        intent.putExtra("followersCount", user.getFollowersNum());
        currentActivity.startActivity(intent);
    }
}