package com.zio.ziospace.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zio.ziospace.R;
import com.zio.ziospace.data.ModelZBitLesson;
import com.zio.ziospace.data.ModelZBitTopic;
import com.zio.ziospace.ui.activities.LessonsActivity;

import java.util.ArrayList;

public class AdapterBits extends RecyclerView.Adapter<AdapterBits.MyHolder> {
    private final Context context;
    private final ArrayList<ModelZBitTopic> modelZBitTopics;

    public AdapterBits(Context context, ArrayList<ModelZBitTopic> list) {
        this.context = context;
        this.modelZBitTopics = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topics, parent, false);
        return new AdapterBits.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        ModelZBitTopic modelZBitTopic = modelZBitTopics.get(position);
        holder.topics.setText(modelZBitTopic.getTopic());
        StringBuilder subsBuilder = new StringBuilder();
        int i = 0;
        for (ModelZBitLesson lesson : modelZBitTopic.getLessons()) {
            subsBuilder.append(++i).append(". ").append(lesson.getHeading()).append("\n");
        }
        String subs = subsBuilder.toString();
        holder.subtopic.setText(subs);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleTextView(holder.subtopic, holder.ShowMore);
            }
        });

        holder.ShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LessonsActivity.class);
                intent.putExtra("topic", modelZBitTopic);
                context.startActivity(intent);
            }
        });


    }

    private void toggleTextView(TextView textView, ImageButton imageButton) {
        if (textView.getVisibility() == View.GONE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageButton, "rotation", imageButton.getRotation(), 0);
            textView.setVisibility(View.VISIBLE);
            textView.animate().alpha(1.0f).setDuration(500).start();
            animator.setDuration(500); // Set the duration of the animation in milliseconds
            animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageButton, "rotation", imageButton.getRotation(), 90);
            textView.setVisibility(View.GONE);
            textView.animate().alpha(0.0f).setDuration(500).start();
            animator.setDuration(500); // Set the duration of the animation in milliseconds
            animator.start();
        }
    }

    @Override
    public int getItemCount() {
        return modelZBitTopics.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView subtopic, topics;
        ImageView status_img;
        ImageButton ShowMore;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            topics = itemView.findViewById(R.id.p_topic);
            subtopic = itemView.findViewById(R.id.lsn_name);
            status_img = itemView.findViewById(R.id.fin_set);
            ShowMore = itemView.findViewById(R.id.btn_show);
        }
    }
}
