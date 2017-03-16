package com.example.naresh.demoproject_1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naresh on 27/2/17.
 */

public class User implements Parcelable {


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

    private int userId;

    @SerializedName("badge_counts")
    private BadgeCounts badgeCounts;

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
        dest.writeParcelable(this.badgeCounts, flags);

    }

    private User(Parcel in) {
        this.profileImage = in.readString();
        this.displayName = in.readString();
        this.reputation = in.readInt();
        this.userId=in.readInt();
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