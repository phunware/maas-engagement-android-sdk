package com.phunware.engagement.sample.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Config implements Parcelable {
    public String name;
    public String appId;
    public String accessKey;

    public Config(String name, String appId, String accessKey) {
        this.name = name;
        this.appId = appId;
        this.accessKey = accessKey;
    }

    @Override
    public String toString() {
        return name;
    }

    protected Config(Parcel in) {
        name = in.readString();
        appId = in.readString();
        accessKey = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(appId);
        dest.writeString(accessKey);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Config> CREATOR =
        new Parcelable.Creator<Config>() {
            @Override
            public Config createFromParcel(Parcel in) {
                return new Config(in);
            }

            @Override
            public Config[] newArray(int size) {
                return new Config[size];
            }
        };
}
