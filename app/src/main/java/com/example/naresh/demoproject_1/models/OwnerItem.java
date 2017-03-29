package com.example.naresh.demoproject_1.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naresh on 24/3/17.
 */

public class OwnerItem {
    @SerializedName("display_name")

    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
