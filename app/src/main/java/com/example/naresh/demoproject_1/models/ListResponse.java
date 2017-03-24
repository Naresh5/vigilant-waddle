package com.example.naresh.demoproject_1.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by naresh on 24/3/17.
 */
public class ListResponse<T> {

        private ArrayList<T> items;

        @SerializedName("has_more")
        private boolean hasMore;

        public void setItems(ArrayList<T> items) {
            this.items = items;
        }

        public ArrayList<T> getItems() {
            return items;
        }

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }
    }
