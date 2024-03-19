package com.zio.ziospace.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ModelZBitTopic implements Parcelable {
    private String topic;
    private ArrayList<ModelZBitLesson> lessons;

    public ModelZBitTopic() {
    }

    public ModelZBitTopic(String topic, ArrayList<ModelZBitLesson> lessons) {
        this.topic = topic;
        this.lessons = lessons;
    }

    protected ModelZBitTopic(Parcel in) {
        topic = in.readString();
        lessons = new ArrayList<>();
        in.readTypedList(lessons, ModelZBitLesson.CREATOR);
    }

    public static final Creator<ModelZBitTopic> CREATOR = new Creator<ModelZBitTopic>() {
        @Override
        public ModelZBitTopic createFromParcel(Parcel in) {
            return new ModelZBitTopic(in);
        }

        @Override
        public ModelZBitTopic[] newArray(int size) {
            return new ModelZBitTopic[size];
        }
    };

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<ModelZBitLesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<ModelZBitLesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeString(topic);
        parcel.writeTypedList(lessons);
    }
}
