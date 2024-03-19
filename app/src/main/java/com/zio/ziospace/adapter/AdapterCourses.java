package com.zio.ziospace.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zio.ziospace.R;
import com.zio.ziospace.data.ModelCourse;
import com.zio.ziospace.ui.course.CourseActivity;
import com.zio.ziospace.ui.dashboard.CourseDet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdapterCourses extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ModelCourse> courseList;
    int type;

    public AdapterCourses(Context context, List<ModelCourse> courseList, int type) {
        this.context = context;
        this.courseList = courseList;
        this.type = type;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (type == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
            return new AllCourseHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_mycourses, parent, false);
            return new MyCourseHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        ModelCourse course = courseList.get(i);
        String title = course.getCtitle();
        String id = course.getCid();
        String imguri = course.getCimage();

        if (type == 1) {
            String desc = course.getCdesc();

            //set data
            ((AllCourseHolder) holder).CourseTitle.setText(title);
            ((AllCourseHolder) holder).CourseDesc.setText(desc);
            Glide.with(context)
                    .load(imguri)
                    .into(((AllCourseHolder) holder).CoursePic);

            ((AllCourseHolder) holder).CourseCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CourseDet.class);
                    intent.putExtra("course", course);
                    context.startActivity(intent);
                }
            });
        } else {

            ((MyCourseHolder) holder).CourseTitle.setText(title);
            Glide.with(context)
                    .load(imguri)
                    .into(((MyCourseHolder) holder).CoursePic);
            progressbarfun(((MyCourseHolder) holder).CourseProgressBar, course.getCsdate(), course.getCedate());
            ((MyCourseHolder) holder).ButtonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, CourseActivity.class);
                    i.putExtra("course", course);
                    context.startActivity(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    private void progressbarfun(ProgressBar pbar, String startd, String endd) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date_start = sdf.parse(startd);
            Date date_end = sdf.parse(endd);
            Calendar calendarstart = Calendar.getInstance();
            Calendar calendarend = Calendar.getInstance();
            calendarstart.setTime(date_start);
            calendarend.setTime(date_end);

            long start = calendarstart.getTimeInMillis();
            long end = calendarend.getTimeInMillis();
            long current = System.currentTimeMillis();

            int progress = (int) ((current - start) * 100 / (end - start));
            pbar.setProgress(progress);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static class AllCourseHolder extends RecyclerView.ViewHolder {

        ImageView CoursePic;
        LinearLayout CourseCard;
        TextView CourseTitle, CourseDesc;

        public AllCourseHolder(@NonNull View itemView) {
            super(itemView);
            CourseCard = itemView.findViewById(R.id.coursecard);
            CoursePic = itemView.findViewById(R.id.pic);
            CourseTitle = itemView.findViewById(R.id.title);
            CourseDesc = itemView.findViewById(R.id.desc);
        }
    }

    static class MyCourseHolder extends RecyclerView.ViewHolder {

        ImageView CoursePic;
        TextView CourseTitle;
        ImageButton ButtonStart;
        ProgressBar CourseProgressBar;
        Button cmpl_start;

        public MyCourseHolder(@NonNull View itemView) {
            super(itemView);

            CourseTitle = itemView.findViewById(R.id.myc_title);
            CoursePic = itemView.findViewById(R.id.myc_img);
            ButtonStart = itemView.findViewById(R.id.myc_start_btn);
            CourseProgressBar = itemView.findViewById(R.id.progressBar2);

        }
    }

}
