package com.example.naresh.demoproject_1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naresh on 27/2/17.
 */

public class User implements Parcelable {

    public User(){

    }

    public User(String displayName, String profileImage, int reputation, BadgeCounts badgeCounts) {
        this.displayName = displayName;
        this.profileImage = profileImage;
        this.reputation = reputation;
        this.badgeCounts = badgeCounts;
    }

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("display_name")
    private String displayName;

    private long reputation;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("answer_count")
    private long answerCount ;

    @SerializedName("question_count")
    private long questionCount ;

    @SerializedName("view_count")
    private long viewCount;

    @SerializedName("badge_counts")
    private BadgeCounts badgeCounts;

    @SerializedName("location")
    private String location;

    @SerializedName("website_url")
    private String websiteUrl;

    @SerializedName("about_me")
    private String aboutMe;

    @SerializedName("link")
    private String profileLink;

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    /*public User(Context context, List<User> userList) {

    }*/

    public String getProfileImage() {
        return profileImage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getReputation() {
        String repString;
        if (reputation < 1000) {
            repString = String.valueOf(reputation);

        } else if (reputation < 10000) {

            String StrReputation = String.valueOf(reputation);

            repString = StrReputation.charAt(0) + ',' + StrReputation.substring(1);

        } else {
            repString = (Math.round((reputation / 1000) * 10) / 10) + "k";
        }
        return repString;
    }

    public BadgeCounts getBadgeCounts() {
        return badgeCounts;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setReputation(long reputation) {
        this.reputation = reputation;
    }

    public void setBadgeCounts(BadgeCounts badgeCounts) {
        this.badgeCounts = badgeCounts;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(long answerCount) {
        this.answerCount = answerCount;
    }

    public long getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(long questionCount) {
        this.questionCount = questionCount;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.profileImage);
        dest.writeString(this.displayName);
        dest.writeLong(this.reputation);
        dest.writeInt(this.userId);
        dest.writeString(this.profileLink);
        dest.writeParcelable(this.badgeCounts, flags);
    }

    protected User(Parcel in) {
        this.profileImage = in.readString();
        this.displayName = in.readString();
        this.reputation = in.readLong();
        this.userId = in.readInt();
        this.profileLink = in.readString();
        this.badgeCounts = in.readParcelable(BadgeCounts.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}