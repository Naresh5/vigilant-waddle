package com.example.naresh.demoproject_1.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by naresh on 28/2/17.
 */

public class BadgeCounts implements Parcelable{

    private int bronze;
    private int silver;
    private int gold;

    public int getBronze() {
        return bronze;
    }

    public int getSilver() {
        return silver;
    }

    public int getGold() {
        return gold;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bronze);
        dest.writeInt(this.silver);
        dest.writeInt(this.gold);
    }

    public BadgeCounts() {
    }

    private BadgeCounts(Parcel in) {
        this.bronze = in.readInt();
        this.silver = in.readInt();
        this.gold = in.readInt();
    }

    public static final Creator<BadgeCounts> CREATOR = new Creator<BadgeCounts>() {
        @Override
        public BadgeCounts createFromParcel(Parcel source) {
            return new BadgeCounts(source);
        }

        @Override
        public BadgeCounts[] newArray(int size) {
            return new BadgeCounts[size];
        }
    };
}


