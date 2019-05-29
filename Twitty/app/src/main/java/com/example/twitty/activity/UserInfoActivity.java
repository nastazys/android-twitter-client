package com.example.twitty.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitty.adapter.TweetAdapter;
import com.example.twitty.pojo.SimpleUser;
import com.squareup.picasso.Picasso;

import com.example.twitty.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import retrofit2.Call;

public class UserInfoActivity extends AppCompatActivity {

    private Long userId;
    // private ImageView userBackView;
    private ImageView userImageView;
    private TextView nameTextView;
    private TextView nickTextView;
    private TextView descriptionTextView;
    private TextView followingCountTextView;
    private TextView followersCountTextView;
    private Toolbar toolbar;

    private RecyclerView tweetsRecyclerView;
    private TweetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userId = getIntent().getExtras().getLong("userId");

        userImageView = findViewById(R.id.user_image_view);
        nameTextView = findViewById(R.id.user_name_text_view);
        nickTextView = findViewById(R.id.user_nick_text_view);
        descriptionTextView = findViewById(R.id.user_description_text_view);
        followingCountTextView = findViewById(R.id.following_count_text_view);
        followersCountTextView = findViewById(R.id.followers_count_text_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new TweetAdapter(UserInfoActivity.this);

        getTweets();
        displayTweets();
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
        return true;
    }

    private void getTweets() {
        final StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();
        Call<List<Tweet>> list = statusesService
                .userTimeline(userId, null, 800, null, null, false, false, false, null);
        list.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                adapter.setItems(result.data);
                displayUserInfo(new SimpleUser(result.data.get(0).user));
            }

            @Override
            public void failure(TwitterException exception) {
            }
        });
    }

    private void displayTweets() {
        tweetsRecyclerView = findViewById(R.id.tweets_recycler_view);
        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetsRecyclerView.setAdapter(adapter);
    }

    private void displayUserInfo(SimpleUser user) {
        Picasso.get().load(user.getImageUrl()).into(userImageView);
        nameTextView.setText(user.getName());
        nickTextView.setText(user.getNick());
        descriptionTextView.setText(user.getDescription());

        String followingCount = String.valueOf(user.getFollowingNum());
        followingCountTextView.setText(followingCount);

        String followersCount = String.valueOf(user.getFollowersNum());
        followersCountTextView.setText(followersCount);

        getSupportActionBar().setTitle(user.getName());
    }

}