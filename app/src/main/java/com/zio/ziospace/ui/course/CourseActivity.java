package com.zio.ziospace.ui.course;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zio.ziospace.R;
import com.zio.ziospace.components.AppBar;
import com.zio.ziospace.data.ModelCourse;

public class CourseActivity extends FragmentActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    AppBar titleView;

    SharedPreferences preferences;
    private String link, CID, NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);

        //hooks
        tabLayout = findViewById(R.id.tabview);
        viewPager = findViewById(R.id.vpager);
        titleView = findViewById(R.id.cvappbar);


        Intent i = getIntent();
        ModelCourse course = i.getParcelableExtra("course");

        CID = course.getCid();
        NAME = course.getCtitle();
        link = course.getClink();
        titleView.setTitle(NAME);

        FragBlocks f1 = new FragBlocks(CID, link);
        FragTasks f2 = new FragTasks(CID);
        FragResource f3 = new FragResource(CID);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return 3;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {

                switch (position) {
                    default:
                        return f1;
                    case 1:
                        return f2;
                    case 2:
                        return f3;
                }
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Blocks");
                        break;
                    case 1:
                        tab.setText("Tasks");
                        break;
                    case 2:
                        tab.setText("Resource");
                }
            }
        }).attach();

        /*preferences = getSharedPreferences("course", MODE_PRIVATE);
        if (preferences.getBoolean(CID, true))
            subscribe_topic();

         */


    }

    //VERY IMPORTANT TO NOTE ADDED IN MANIFEST FOR FILE PROVIDERS AND XML RES FILE FOR PATH

    public void subscribe_topic() {
        SharedPreferences.Editor ed = preferences.edit();
        int size = preferences.getInt("crs_size", 0);
        ed.putString("course" + size, CID);
        ed.putInt("crs_size", size + 1);
        ed.apply();

        FirebaseMessaging.getInstance().subscribeToTopic(CID).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ed.putBoolean(CID, false).apply();
            }
        });
    }

    public void goback(View view) {
        finish();
    }

    public void open_onclick(View view) {
       /* switch (view.getId()) {

            case R.id.mat_btn:
                Intent intent1 = new Intent(CourseActivity.this, Downloads.class);
                intent1.putExtra("course_name", NAME);
                startActivity(intent1);
                break;
            case R.id.teams_btn:
                Intent intent = new Intent(CourseActivity.this, ActTeamShare.class);
                intent.putExtra("cid", CID);
                startActivity(intent);
                break;
        }

        */
    }


}