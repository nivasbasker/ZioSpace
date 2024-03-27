package com.zio.ziospace.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zio.ziospace.R;
import com.zio.ziospace.common.LoginManager;
import com.zio.ziospace.ui.zbits.BitsActivity;
import com.zio.ziospace.helpers.Constants;

public class DashboardActivity extends AppCompatActivity {

    BottomNavigationView navigationbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        check_security();

        navigationbar = findViewById(R.id.btm_menu);
        navigationbar.setSelectedItemId(R.id.menu_1);

        Fragment exploreFragment = new FragExplore();
        Fragment myCoursesFragment = new FragMyCourses();
        Fragment profileFragment = new FragProfile();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, exploreFragment).commit();
        navigationbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.menu_1:
                        fragment = exploreFragment;
                        break;
                    case R.id.menu_2:
                        fragment = myCoursesFragment;
                        break;
                    case R.id.menu_4:
                        fragment = profileFragment;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).commit();
                return true;
            }
        });

    }

    private final BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.LOGOUT_ACTION)) {
                finish(); // Finish the activity on logout
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(logoutReceiver, new IntentFilter(Constants.LOGOUT_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(logoutReceiver);
    }

    private void check_security() {
        String user = LoginManager.getUserName(this);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("USERINFO").child(user);
        ref.child("logout").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue(Boolean.class)) {
                    LoginManager sm = new LoginManager(DashboardActivity.this);
                    sm.logoutUser();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void openBits(View v) {
        startActivity(new Intent(this, BitsActivity.class));
    }

}