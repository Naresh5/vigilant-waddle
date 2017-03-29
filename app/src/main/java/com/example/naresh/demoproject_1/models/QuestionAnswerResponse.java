package com.example.naresh.demoproject_1.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by naresh on 20/3/17.
 */

public class QuestionAnswerResponse {

    private ArrayList<QuestionItem> items;

    @SerializedName("has_more")
    private boolean hasMore;


    public ArrayList<QuestionItem> getItems() {
        return items;
    }

    public boolean isHasMore() {
        return hasMore;
    }


}
