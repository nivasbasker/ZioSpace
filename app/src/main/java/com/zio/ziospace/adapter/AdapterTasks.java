package com.zio.ziospace.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.zio.ziospace.R;
import com.zio.ziospace.data.ModelTask;
import com.zio.ziospace.ui.course.CourseTask;

import java.util.List;

public class AdapterTasks extends RecyclerView.Adapter<AdapterTasks.ViewHolder> {

    private final Context context;
    private final List<ModelTask> taskList;
    private final String CID;

    public AdapterTasks(Context context, List<ModelTask> tasksList, String CID) {
        this.context = context;
        this.taskList = tasksList;
        this.CID = CID;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ModelTask ThisTask = taskList.get(position);
        holder.title.setText(ThisTask.getTitle());

        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseTask.class);
                intent.putExtra("cid", CID);
                intent.putExtra("task", ThisTask);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        Button open;
        ImageView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ex_topic);
            open = itemView.findViewById(R.id.ex_open_btn);
            status = itemView.findViewById(R.id.ex_status_img);

        }
    }
}
