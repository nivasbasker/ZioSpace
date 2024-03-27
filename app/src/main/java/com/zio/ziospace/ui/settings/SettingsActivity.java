package com.zio.ziospace.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zio.ziospace.R;
import com.zio.ziospace.common.LoginManager;

public class SettingsActivity extends AppCompatActivity {

    private SwitchMaterial notif_switch_register, notif_swtich_general;
    Button theme_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        notif_switch_register = findViewById(R.id.switch1);
        notif_swtich_general = findViewById(R.id.switch2);
        theme_button = findViewById(R.id.chs_theme);

        SharedPreferences sharedPrefs = getSharedPreferences("switches", MODE_PRIVATE);
        notif_switch_register.setChecked(sharedPrefs.getBoolean("switch1", true));
        notif_swtich_general.setChecked(sharedPrefs.getBoolean("switch2", true));
        SharedPreferences.Editor editor = sharedPrefs.edit();

        notif_switch_register.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("switch1", notif_switch_register.isChecked()).apply();
        });

        notif_swtich_general.setOnCheckedChangeListener((compoundButton, b) -> {
            if (notif_swtich_general.isChecked()) {
                FirebaseMessaging.getInstance().subscribeToTopic("general");
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("general");
            }
            editor.putBoolean("switch2", notif_swtich_general.isChecked()).apply();
        });


    }

    public void click(View view) {
        switch (view.getId()) {
            /*case R.id.edit_prof:
                startActivity(new Intent(this, EditProfile.class));
                break;
            case R.id.change_pass:
                startActivity(new Intent(this, ChangePass.class));
                break;
            case R.id.mentor:
                startActivity(new Intent(SettingsActivity.this, BecomeMentorActivity.class));
                break;
            case R.id.help:
                startActivity(new Intent(SettingsActivity.this, HelpMenu.class));
                break;*/
            case R.id.terms:
                browse_site("https://www.codewithzio.com/terms-and-conditions");
                break;
            case R.id.policy:
                browse_site("https://www.codewithzio.com/privacy-policy");
                break;
            case R.id.share_app:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Hey, I've been using the MyZio app for learning some new and interesting things, you must check this out \n " +
                        "https://play.google.com/store/apps/details?id=com.codewithzio.zio";
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share Through "));
                break;
            case R.id.logout1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("LOGOUT ?")
                        .setMessage("Are you sure you want to logout ?")
                        .setCancelable(false)
                        .setPositiveButton(" yes ", (dialogInterface, i) -> {
                            LoginManager sm = new LoginManager(this);
                            sm.logoutUser();
                            finish();
                        }).setNegativeButton(" No ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                AlertDialog ad = builder.create();
                ad.show();
                break;
        }
    }

    private void browse_site(String url) {
        Intent regonsite = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        regonsite.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(regonsite);
    }

}