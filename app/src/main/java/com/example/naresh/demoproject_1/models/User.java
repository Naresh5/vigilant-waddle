package com.example.naresh.demoproject_1.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naresh on 27/2/17.
 */

public class User {
    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("display_name")
    private String displayName;

    private long reputation;

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
}
