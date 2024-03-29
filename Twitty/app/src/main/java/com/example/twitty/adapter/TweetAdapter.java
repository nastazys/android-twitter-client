package com.example.twitty.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitty.R;
import com.example.twitty.activity.UserInfoActivity;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.internal.UserUtils;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.FavoriteService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.internal.TweetMediaUtils;

import retrofit2.Call;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {
    private static final String TWITTER_RESPONSE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"; // Thu Oct 26 07:31:08 +0000 2017
    private static final String MONTH_DAY_FORMAT = "MMM d"; // Oct 26

    private List<Tweet> tweetList = new ArrayList<>();
    private Context context;

    public TweetAdapter(Context c) {
        context = c;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_item_view, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.bind(tweetList.get(position));
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public void setItems(Collection<Tweet> tweets) {
        tweetList.addAll(tweets);
        notifyDataSetChanged();
    }

    public User getUser() {
        return tweetList.get(0).user;
    }

    public void clearItems() {
        tweetList.clear();
        notifyDataSetChanged();
    }

    class TweetViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImageView;
        private TextView nameTextView;
        private TextView nickTextView;
        private TextView creationDateTextView;
        private TextView contentTextView;
        private ImageView tweetImageView;
        private TextView retweetsTextView;
        private TextView likesTextView;

        private ImageView isLiked;
        private ImageView isRetweeted;

        public TweetViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.profile_image_view);
            nameTextView = itemView.findViewById(R.id.author_name_text_view);
            nickTextView = itemView.findViewById(R.id.author_nick_text_view);
            creationDateTextView = itemView.findViewById(R.id.creation_date_text_view);
            contentTextView = itemView.findViewById(R.id.tweet_content_text_view);
            tweetImageView = itemView.findViewById(R.id.tweet_image_view);
            retweetsTextView = itemView.findViewById(R.id.retweets_text_view);
            likesTextView = itemView.findViewById(R.id.likes_text_view);
            isRetweeted = itemView.findViewById(R.id.retweet_image_view);
            isLiked = itemView.findViewById(R.id.like_image_view);
        }

        public void bind(Tweet tweet) {
            nameTextView.setText(tweet.user.name);
            nickTextView.setText(tweet.user.screenName);
            contentTextView.setText(tweet.text);
            retweetsTextView.setText(String.valueOf(tweet.retweetCount));
            likesTextView.setText(String.valueOf(tweet.favoriteCount));

            String creationDateFormatted = getFormattedDate(tweet.createdAt);
            creationDateTextView.setText(creationDateFormatted);

            Picasso.get().load(UserUtils.getProfileImageUrlHttps(tweet.user,
                    UserUtils.AvatarSize.REASONABLY_SMALL)).into(userImageView);

            if (tweet.favorited) {
                isLiked.setImageResource(R.drawable.like);
            } else {
                isLiked.setImageResource(R.drawable.not_like);
            }
            if (tweet.retweeted) {
                isRetweeted.setImageResource(R.drawable.retweet);
            }else {
                isRetweeted.setImageResource(R.drawable.not_retweet);
            }
            if (TweetMediaUtils.hasPhoto(tweet)) {
                String tweetPhotoUrl = TweetMediaUtils.getPhotoEntity(tweet).mediaUrl;
                Picasso.get().load(tweetPhotoUrl).into(tweetImageView);
            } else {
                tweetImageView.setVisibility(View.GONE);
            }

            linkifyProfile(tweet);
            setLikeAction(tweet);
        }

        private String getFormattedDate(String rawDate) {
            SimpleDateFormat utcFormat = new SimpleDateFormat(TWITTER_RESPONSE_FORMAT, Locale.ROOT);
            SimpleDateFormat displayedFormat = new SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault());
            try {
                Date date = utcFormat.parse(rawDate);
                return displayedFormat.format(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        void linkifyProfile(final Tweet displayTweet) {
            if (displayTweet != null && displayTweet.user != null) {
                userImageView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("userId", displayTweet.user.id);
                    context.startActivity(intent);
                });

                nameTextView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("userId", displayTweet.user.id);
                    context.startActivity(intent);
                });
            }
        }

        void setLikeAction(final Tweet displayTweet) {
            if (displayTweet != null && displayTweet.user != null) {
                isLiked.setOnClickListener(v -> {
                    if (displayTweet.favorited) {
                        unfavorite(displayTweet);
                    } else {
                        favorite(displayTweet);
                    }
                });

                isRetweeted.setOnClickListener(v -> {
                        if (displayTweet.retweeted) {
                            unretweet(displayTweet);
                        } else {
                            retweet(displayTweet);
                        }
                });
            }
        }

        void favorite(Tweet tweet) {
            final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
            FavoriteService favoriteService = TwitterCore.getInstance().getApiClient(session).getFavoriteService();
            Call<Tweet> tweetCall = favoriteService.create
                    (tweet.id, false);
            tweetCall.enqueue(new Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    bind(result.data);
                }

                @Override
                public void failure(TwitterException exception) {
                }
            });
        }

        void unfavorite(Tweet tweet) {
            final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
            FavoriteService favoriteService = TwitterCore.getInstance().getApiClient(session).getFavoriteService();
            Call<Tweet> tweetCall = favoriteService.destroy
                    (tweet.id, false);
            tweetCall.enqueue(new Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    bind(result.data);
                }

                @Override
                public void failure(TwitterException exception) {
                }
            });
        }

        void retweet(Tweet tweet) {
            final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
            StatusesService statusesService = TwitterCore.getInstance().getApiClient(session).getStatusesService();
            Call<Tweet> tweetCall = statusesService.retweet
                    (tweet.id, false);
            tweetCall.enqueue(new Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    bind(result.data);
                }

                @Override
                public void failure(TwitterException exception) {
                }
            });
        }

        void unretweet(Tweet tweet) {
            final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
            StatusesService statusesService = TwitterCore.getInstance().getApiClient(session).getStatusesService();
            Call<Tweet> tweetCall = statusesService.unretweet
                    (tweet.id, false);
            tweetCall.enqueue(new Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    bind(result.data);
                }

                @Override
                public void failure(TwitterException exception) {
                }
            });
        }
    }
}