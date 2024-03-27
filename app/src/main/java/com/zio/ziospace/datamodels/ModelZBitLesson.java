package com.zio.ziospace.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ModelZBitLesson implements Parcelable {

    private String heading, content;

    public ModelZBitLesson(String heading, String content) {
        this.heading = heading;
        this.content = content;
    }

    public ModelZBitLesson() {
    }

    protected ModelZBitLesson(Parcel in) {
        heading = in.readString();
        content = in.readString();
    }

    public static final Creator<ModelZBitLesson> CREATOR = new Creator<ModelZBitLesson>() {
        @Override
        public ModelZBitLesson createFromParcel(Parcel in) {
            return new ModelZBitLesson(in);
        }

        @Override
        public ModelZBitLesson[] newArray(int size) {
            return new ModelZBitLesson[size];
        }
    };

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(heading);
        parcel.writeString(content);
    }
}
