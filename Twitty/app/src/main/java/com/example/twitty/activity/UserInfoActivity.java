package com.example.twitty.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

    void loadTweets() {
        final StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();
        Call<List<Tweet>> list = statusesService
                .userTimeline(userId, null, 800, null, null, false, false, false, null);
        list.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                tweetAdapter.setItems(result.data);
                displayUserInfo(result.data.get(0).user);
            }

            @Override
            public void failure(TwitterException exception) {
            }
        });
    }

    private void displayUserInfo(User user) {
        Picasso.get().load(UserUtils.getProfileImageUrlHttps(user,
                UserUtils.AvatarSize.BIGGER)).into(userImageView);
        nameTextView.setText(user.name);
        nickTextView.setText(user.screenName);
        descriptionTextView.setText(user.description);

        String followingCount = String.valueOf(user.friendsCount);
        followingCountTextView.setText(followingCount);

        String followersCount = String.valueOf(user.followersCount);
        followersCountTextView.setText(followersCount);

        getSupportActionBar().setTitle(user.name);
    }

}