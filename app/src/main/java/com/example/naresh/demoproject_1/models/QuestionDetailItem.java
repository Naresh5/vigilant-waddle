package com.example.naresh.demoproject_1.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by naresh on 24/3/17.
 */

public class QuestionDetailItem {

    @SerializedName("is_answered")
    private boolean isAnswered;
    private String title;
    private int score;
    private String link;
    private ArrayList<String> tags;
    @SerializedName("answer_count")
    private int answerCount;
    @SerializedName("creation_date")
    private int questionCreationDate;
    @SerializedName("owner")
    private OwnerItem ownerItem;

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getQuestionCreationDate() {
        return questionCreationDate;
    }

    public void setQuestionCreationDate(int questionCreationDate) {
        this.questionCreationDate = questionCreationDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public OwnerItem getOwnerItem() {
        return ownerItem;
    }

    public void setOwnerItem(OwnerItem ownerItem) {
        this.ownerItem = ownerItem;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}