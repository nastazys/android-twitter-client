package com.example.twitty.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twitty.R;
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
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;
    TwitterSession session;
    String token;
    String secret;
    Boolean isUserAuthorized = false;

    TextView infoTextView;

    SharedPreferences mShared;

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

        getSessionInfo();
        if (!isUserAuthorized) {
            loginButton = findViewById(R.id.login_button);
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    TwitterAuthToken authToken = session.getAuthToken();
                    saveSessionInfo(authToken.token, authToken.secret);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userId", session.getUserId());
                    startActivity(intent);

                }

                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(LoginActivity.this, "Ошибка авторизации!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            session = TwitterCore.getInstance().getSessionManager().getActiveSession();
            intent.putExtra("userId", session.getUserId());
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void saveSessionInfo(String curToken, String curSecret){
        mShared = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mShared.edit();
        mEditor.putBoolean("isUserAuthorized", true);
        mEditor.putString("token", curToken);
        mEditor.putString("secret", curSecret);
        mEditor.commit();
    }

    private void getSessionInfo(){
        mShared = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mShared.edit();
        isUserAuthorized = mShared.getBoolean("isUserAuthorized", false);
        token = mShared.getString("token", "");
        secret = mShared.getString("secret", "");
    }

}