package com.zio.ziospace.ui.dashboard;


import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zio.ziospace.data.ModelCourse;
import com.zio.ziospace.databinding.DashboardCoursedetBinding;
import com.zio.ziospace.helpers.LoginManager;

import java.util.Date;

public class CourseDet extends AppCompatActivity {

    String CID, MentorName, UserName;

    ImageView cdimg;
    TextView exv;
    TextView cfee;
    DashboardCoursedetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DashboardCoursedetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //hooks
        exv = binding.cdDet;
        cdimg = binding.topimg;
        cfee = binding.cdFee;

        ModelCourse course = getIntent().getParcelableExtra("course");
        CID = course.getCid();
        MentorName = course.getCmentor();
        UserName = LoginManager.getUserName(this);

        exv.setText(course.getCdetail());
        cfee.setText(course.getCprice());
        binding.ctitle.setText(course.getCtitle());
        binding.cdes.setText(course.getCdesc());

        Glide.with(getBaseContext())
                .load(course.getCimage())
                .into(cdimg);

    }

    public void goback(View view) {
        finish();
    }

    public void regcourse(View view) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("USER_MENTOR/" + MentorName + "/" + CID + "/requests");
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd,MM,yyyy");
        String formattedDate = dateFormat.format(currentDate);

        ((TextView) view).setText("Requesting..");

        ref.child(UserName).setValue(formattedDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Snackbar.make(binding.getRoot(), "Enrollment Request Sent, you will be contacted Soon", Snackbar.LENGTH_LONG).show();
                ((TextView) view).setText("Request Sent");
                ((TextView) view).setEnabled(false);
            }
        });
    }

    public void help(View view) {
        //startActivity(new Intent(CourseDet.this, HelpMenu.class));
    }

    public void share(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = "Hey, I've been using the MyZio app for learning some new and interesting things, you must check this out \n " +
                "https://play.google.com/store/apps/details?id=com.codewithzio.zio";
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Share Through "));

    }
}