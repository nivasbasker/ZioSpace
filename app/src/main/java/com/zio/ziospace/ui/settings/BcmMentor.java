package com.zio.ziospace.ui.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zio.ziospace.R;
import com.zio.ziospace.helpers.LoginManager;

public class BcmMentor extends AppCompatActivity {

    DatabaseReference analytic;
    String USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_bcm_mentor);

        USERNAME = LoginManager.getUserName(this);

        analytic = FirebaseDatabase.getInstance().getReference().child("analytics").child("bcm");
        analytic.child(USERNAME).setValue(false);

    }

    public void goback(View view) {
        finish();
    }

    public void send_resume(View view) {

        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ziosoftwares@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, " My Resume ");
            intent.putExtra(Intent.EXTRA_TEXT, "Name :\nAge :\nQualification :\nField of Expertise :\nAbout Myself :\nresidential address :\nContact no :");
            startActivity(intent);
            analytic.child(USERNAME).setValue(true);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "please install any email app ", Toast.LENGTH_SHORT).show();
        }

    }
}