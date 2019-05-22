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

import com.example.twitty.pojo.SimpleUser;
import com.squareup.picasso.Picasso;

import com.example.twitty.R;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.UserTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;

public class UserInfoActivity extends AppCompatActivity {

    private long userId;
    private SimpleUser user;
    private ImageView userImageView;
    private TextView nameTextView;
    private TextView nickTextView;
    private TextView descriptionTextView;
    private TextView followingCountTextView;
    private TextView followersCountTextView;
    private Toolbar toolbar;

    private RecyclerView tweetsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        //userId = getIntent().getExtras()
        userImageView = findViewById(R.id.user_image_view);
        nameTextView = findViewById(R.id.user_name_text_view);
        nickTextView = findViewById(R.id.user_nick_text_view);
        descriptionTextView = findViewById(R.id.user_description_text_view);
        followingCountTextView = findViewById(R.id.following_count_text_view);
        followersCountTextView = findViewById(R.id.followers_count_text_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initRecyclerView();

        displayUserInfo();
        displayTweets();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(this, SearchUsersActivity.class);
            startActivity(intent);
        }
        return true;
    }*/

    private void displayTweets() {
        UserTimeline userTimeline = new UserTimeline.Builder().userId(userId).build();
        TweetTimelineRecyclerViewAdapter adapter =
                new TweetTimelineRecyclerViewAdapter.Builder(this)
                        .setTimeline(userTimeline)
                        .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                        .build();
        tweetsRecyclerView.setAdapter(adapter);
    }
    private void initRecyclerView() {
        tweetsRecyclerView = findViewById(R.id.tweets_recycler_view);
        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void displayUserInfo() {
        getUser();

        Picasso.get().load(user.getImageUrl()).into(userImageView);
        nameTextView.setText(user.getName());
        //!!!!nickTextView.setText(user.getNick());
        descriptionTextView.setText(user.getDescription());

        String followingCount = String.valueOf(user.getFollowingNum());
        followingCountTextView.setText(followingCount);

        String followersCount = String.valueOf(user.getFollowersNum());
        followersCountTextView.setText(followersCount);

        getSupportActionBar().setTitle(user.getName());
    }

    private void getUser() {
        Bundle extras = getIntent().getExtras();
        user = new SimpleUser(extras.getLong("id"),
                extras.getString("UserName"),
                "nick",
                extras.getString("profileImageUrl"),
                extras.getString("description"),
                extras.getInt("followersCount"),
                extras.getInt("followingCount"));
    }

}