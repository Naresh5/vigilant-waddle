package com.example.naresh.demoproject_1.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by naresh on 1/3/17.
 */

public class UserResponse {

    private ArrayList<User> items;
    @SerializedName("has_more")
    private boolean hasMore;

    public ArrayList<User> getItems() {
        return items;
    }

    public boolean isHasMore() {
        return hasMore;
    }

}
