package com.zio.ziospace.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ModelTask implements Parcelable {
    private String title, todo, taskid;
    private int type;

    public ModelTask() {
    }

    public ModelTask(String title, String todo, String taskid, int type) {
        this.title = title;
        this.todo = todo;
        this.taskid = taskid;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(todo);
        parcel.writeString(taskid);
        parcel.writeInt(type);
    }

    public static final Parcelable.Creator<ModelTask> CREATOR
            = new Parcelable.Creator<ModelTask>() {
        public ModelTask createFromParcel(Parcel in) {
            return new ModelTask(in);
        }

        public ModelTask[] newArray(int size) {
            return new ModelTask[size];
        }
    };

    private ModelTask(Parcel in) {
        title = in.readString();
        todo = in.readString();
        taskid = in.readString();
        type = in.readInt();
    }
}
