package com.example.twitty.pojo;
import com.twitter.sdk.android.core.models.User;

public class SimpleUser {
    private long id = 458472903;
    private String name = "Nastassia";
    private String nick = "nastazys";
    private String imageUrl = "https://pbs.twimg.com/profile_images/900378926626873344/TVnwWtvw_400x400.jpg";
    private String description = "descriptionnn";
    private Integer followersNum = 888;
    private Integer followingNum = 88;

    public SimpleUser(long id, String name, String nick, String imageUrl, String description, Integer followersNum, Integer followingNum) {
        this.id = id;
        this.name = name;
        this.nick = nick;
        this.imageUrl = imageUrl;
        this.description = description;
        this.followersNum = followersNum;
        this.followingNum = followingNum;
    }

    public SimpleUser(User twitterUser){
        this.id = twitterUser.id;
        this.name = twitterUser.name;
        this.nick = twitterUser.screenName;
        this.imageUrl = twitterUser.profileImageUrl;
        this.description = twitterUser.description;
        this.followersNum = twitterUser.followersCount;
        this.followingNum = twitterUser.friendsCount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNick() {
        return nick;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public Integer getFollowersNum() {
        return followersNum;
    }

    public Integer getFollowingNum() {
        return followingNum;
    }

}
