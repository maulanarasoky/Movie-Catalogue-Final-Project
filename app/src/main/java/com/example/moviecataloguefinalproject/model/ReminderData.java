package com.example.moviecataloguefinalproject.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReminderData implements Parcelable {
    private boolean conditionDaily;
    private boolean conditionRelease;

    public boolean isConditionDaily() {
        return conditionDaily;
    }

    public void setConditionDaily(boolean conditionDaily) {
        this.conditionDaily = conditionDaily;
    }

    public boolean isConditionRelease() {
        return conditionRelease;
    }

    public void setConditionRelease(boolean conditionRelease) {
        this.conditionRelease = conditionRelease;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.conditionDaily ? (byte) 1 : (byte) 0);
        dest.writeByte(this.conditionRelease ? (byte) 1 : (byte) 0);
    }

    public ReminderData() {
    }

    private ReminderData(Parcel in) {
        this.conditionDaily = in.readByte() != 0;
        this.conditionRelease = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ReminderData> CREATOR = new Parcelable.Creator<ReminderData>() {
        @Override
        public ReminderData createFromParcel(Parcel source) {
            return new ReminderData(source);
        }

        @Override
        public ReminderData[] newArray(int size) {
            return new ReminderData[size];
        }
    };
}
