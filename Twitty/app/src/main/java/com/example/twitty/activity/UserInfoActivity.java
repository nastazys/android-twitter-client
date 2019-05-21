package com.example.twitty.activity;

import java.util.Arrays;
import java.util.Collection;

import android.annotation.SuppressLint;
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

import com.squareup.picasso.Picasso;

import com.example.twitty.R;
//import colibri.dev.com.colibritweet.adapter.TweetAdapter;
import com.example.twitty.pojo.Tweet;
import com.example.twitty.pojo.User;

public class UserInfoActivity extends AppCompatActivity {

    public static final String USER_ID = "userId";

    private ImageView userImageView;
    private TextView nameTextView;
    private TextView nickTextView;
    private TextView descriptionTextView;
    private TextView locationTextView;
    private TextView followingCountTextView;
    private TextView followersCountTextView;
    private Toolbar toolbar;

    private RecyclerView tweetsRecyclerView;
    //private TweetAdapter tweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userImageView = findViewById(R.id.user_image_view);
        nameTextView = findViewById(R.id.user_name_text_view);
        nickTextView = findViewById(R.id.user_nick_text_view);
        descriptionTextView = findViewById(R.id.user_description_text_view);
        followingCountTextView = findViewById(R.id.following_count_text_view);
        followersCountTextView = findViewById(R.id.followers_count_text_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        initRecyclerView();

        loadUserInfo();
        loadTweets();*/
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
    }

    private void loadTweets() {
        Collection<Tweet> tweets = getTweets();
        tweetAdapter.setItems(tweets);
    }

    private Collection<Tweet> getTweets() {
        return Arrays.asList(
                new Tweet(getUser(), 1L, "Thu Dec 13 07:31:08 +0000 2017", "Очень длинное описание твита 1",
                        4L, 4L, "https://www.w3schools.com/w3css/img_fjords.jpg"),

                new Tweet(getUser(), 2L, "Thu Dec 12 07:31:08 +0000 2017", "Очень длинное описание твита 2",
                        5L, 5L, "https://www.w3schools.com/w3images/lights.jpg"),

                new Tweet(getUser(), 3L, "Thu Dec 11 07:31:08 +0000 2017", "Очень длинное описание твита 3",
                        6L, 6L, "https://www.w3schools.com/css/img_mountains.jpg")
        );
    }

    private void initRecyclerView() {
        tweetsRecyclerView = findViewById(R.id.tweets_recycler_view);
        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetAdapter = new TweetAdapter();
        tweetsRecyclerView.setAdapter(tweetAdapter);
    }

    private void loadUserInfo() {
        User user = getUser();
        displayUserInfo(user);
    }

    private void displayUserInfo(User user) {
        Picasso.with(this).load(user.getImageUrl()).into(userImageView);
        nameTextView.setText(user.getName());
        nickTextView.setText(user.getNick());
        descriptionTextView.setText(user.getDescription());
        locationTextView.setText(user.getLocation());

        String followingCount = String.valueOf(user.getFollowingCount());
        followingCountTextView.setText(followingCount);

        String followersCount = String.valueOf(user.getFollowersCount());
        followersCountTextView.setText(followersCount);

        getSupportActionBar().setTitle(user.getName());
    }

    private User getUser() {
        return new User(
                1L,
                "http://i.imgur.com/DvpvklR.png",
                "DevColibri",
                "devcolibri",
                "Sample description",
                "USA",
                42,
                42
        );
    }
    */
}