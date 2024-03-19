package com.zio.ziospace.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zio.ziospace.R;

public class AppBar extends LinearLayout {

    TextView titleview;
    ImageButton LeftIcon, RightIcon;
    Context context;
    AttributeSet attrs;

    public AppBar(Context context) {
        super(context);
        this.context = context;
        initial();
    }

    public AppBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initial();
    }

    private void initial() {
        inflate(getContext(), R.layout.component_appbar, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppBar);
        boolean needBack = a.getBoolean(R.styleable.AppBar_backEnable, false);
        String Title = a.getString(R.styleable.AppBar_Title);

        titleview = findViewById(R.id.appbartitle);
        LeftIcon = findViewById(R.id.appbarlefticon);

        titleview.setText(Title);
        if (needBack) {
            LeftIcon.setVisibility(VISIBLE);
            LeftIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity = (Activity) context;
                    activity.finish();
                }
            });
        }

    }

    public boolean setTitle(String title) {
        titleview.setText(title);
        return true;
    }
}
