package com.example.naresh.demoproject_1.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naresh on 20/3/17.
 */

public class QuestionItem {

    private String title;
    private String link;
    private String body;
    @SerializedName("post_type")
    private String postType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }
}

