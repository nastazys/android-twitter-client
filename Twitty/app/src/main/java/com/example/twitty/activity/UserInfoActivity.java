package com.example.twitty.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitty.task.UserInfoTask;
import com.squareup.picasso.Picasso;

import com.example.twitty.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.UserUtils;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import retrofit2.Call;

public class UserInfoActivity extends StandardTimelineActivity {

    private Long userId;

    private ImageView userImageView;
    private TextView nameTextView;
    private TextView nickTextView;
    private TextView descriptionTextView;
    private TextView followingCountTextView;
    private TextView followersCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_info);
        userId = getIntent().getExtras().getLong("userId");

        userImageView = findViewById(R.id.user_image_view);
        nameTextView = findViewById(R.id.user_name_text_view);
        nickTextView = findViewById(R.id.user_nick_text_view);
        descriptionTextView = findViewById(R.id.user_description_text_view);
        followingCountTextView = findViewById(R.id.following_count_text_view);
        followersCountTextView = findViewById(R.id.followers_count_text_view);

        super.onCreate(savedInstanceState);
    }

    public void displayUserInfo(User user) {
        Picasso.get().load(UserUtils.getProfileImageUrlHttps(user,
                UserUtils.AvatarSize.BIGGER)).into(userImageView);
        nameTextView.setText(user.name);
        nickTextView.setText(user.screenName);
        if (user.description != null) {
            descriptionTextView.setText(user.description);
        } else {
            descriptionTextView.setText(R.string.dummy);
        }

        String followingCount = String.valueOf(user.friendsCount);
        followingCountTextView.setText(followingCount);

        String followersCount = String.valueOf(user.followersCount);
        followersCountTextView.setText(followersCount);

        getSupportActionBar().setTitle(user.name);
    }

    @Override
    void initTask(){
        task = new UserInfoTask(tweetAdapter, userId, this);
    }

}