package com.example.twitty.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.twitty.R;
import com.example.twitty.pojo.SimpleUser;
import com.example.twitty.task.MainUserInfoTask;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import com.twitter.sdk.android.core.models.User;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;
    TwitterSession session;
    SimpleUser user;
    TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_login);
        infoTextView = findViewById(R.id.display);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(
                        getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                        getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        loginButton = findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                session = TwitterCore.getInstance().getSessionManager().getActiveSession();

                /*try {
                    user = new MainUserInfoTask(LoginActivity.this).execute().get();
                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                }*/
                Intent intent = new Intent(LoginActivity.this, UserInfoActivity.class);
                intent.putExtra("userId", session.getUserId());
                startActivity(intent);

            }

            @Override
            public void failure(TwitterException exception) {
                infoTextView.setText(R.string.failed_to_authorize_message);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


    /*class UserInfoTask extends AsyncTask<String, Integer, Void>{
        @Override
        protected Void doInBackground(String[] objects) {
            getMainUser();
            return null;
        }
    }

    public void getMainUser() {
        TwitterApiClient apiClient = new TwitterApiClient(session);
        Call<User> userResult = apiClient.getAccountService()
                .verifyCredentials(true, false, false);
        userResult.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                user = new SimpleUser (result.data);
            }

            @Override
            public void failure(TwitterException exception) {
            }
        });
    }*/

}
