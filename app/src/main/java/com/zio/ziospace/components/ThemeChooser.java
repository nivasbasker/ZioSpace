package com.zio.ziospace.components;

import static android.content.Context.MODE_PRIVATE;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;
import com.zio.ziospace.R;

public class ThemeChooser extends MaterialButton {

    private SharedPreferences sharedPref;
    private final Context context;
    private int prev_mode;

    private OnChangeListener listener;

    private final String PREFERENCE = "DisplayTheme", KEY = "mode";

    private interface OnChangeListener {
        void OnChange(int chosenTheme);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        this.listener = listener;
    }

    public ThemeChooser(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ThemeChooser(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ThemeChooser(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        sharedPref = context.getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        prev_mode = sharedPref.getInt(KEY, MODE_NIGHT_NO);
        String label = "Display Theme : ";
        switch (prev_mode) {
            case MODE_NIGHT_NO:
                label = label.concat("Light");
                break;
            case MODE_NIGHT_YES:
                label = label.concat("Dark");
                break;
            case MODE_NIGHT_FOLLOW_SYSTEM:
                label = label.concat("Default");
                break;
        }
        this.setText(label);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
    }


    private void showCustomDialog() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_theme, null);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
        RadioButton radioButton1 = dialogView.findViewById(R.id.radioButton1);
        RadioButton radioButton2 = dialogView.findViewById(R.id.radioButton2);
        RadioButton radioButton3 = dialogView.findViewById(R.id.radioButton3);

        switch (prev_mode) {
            case MODE_NIGHT_NO:
                radioButton1.setChecked(true);
                break;
            case MODE_NIGHT_YES:
                radioButton2.setChecked(true);
                break;
            case MODE_NIGHT_FOLLOW_SYSTEM:
                radioButton3.setChecked(true);
                break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogView.findViewById(R.id.rcancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.rokay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                int MODE = MODE_NIGHT_AUTO_BATTERY;
                if (selectedId == radioButton1.getId()) {
                    MODE = MODE_NIGHT_NO;
                } else if (selectedId == radioButton2.getId()) {
                    MODE = MODE_NIGHT_YES;
                } else if (selectedId == radioButton3.getId()) {
                    MODE = MODE_NIGHT_FOLLOW_SYSTEM;
                }
                sharedPref.edit().putInt(KEY, MODE).apply();
                AppCompatDelegate.setDefaultNightMode(MODE);
                if (listener != null) listener.OnChange(MODE);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
