package com.zio.ziospace.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.zio.ziospace.data.ModelZBitTopic;
import com.zio.ziospace.adapter.AdapterLessons;
import com.zio.ziospace.databinding.ActivityLessonsBinding;

public class LessonsActivity extends AppCompatActivity {

    private ActivityLessonsBinding binding;
    private ModelZBitTopic modelZBitTopic;
    private AdapterLessons adapter;
    private ViewPager lesson_view;

    private LottieAnimationView finish_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLessonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        modelZBitTopic = getIntent().getParcelableExtra("topic");

        lesson_view = binding.lsnsViewPager;
        finish_anim = binding.finishAnim;
        adapter = new AdapterLessons(this, modelZBitTopic.getLessons());
        lesson_view.setAdapter(adapter);
    }

    public void next(View v) {

        int currentItem = lesson_view.getCurrentItem();
        if (currentItem < modelZBitTopic.getLessons().size() - 1)
            lesson_view.setCurrentItem(currentItem + 1, true);
        else {
            finish_anim.playAnimation();
            finish_anim.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animator) {

                }

                @Override
                public void onAnimationEnd(@NonNull Animator animator) {
                    finish();
                }

                @Override
                public void onAnimationCancel(@NonNull Animator animator) {

                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animator) {

                }
            });
        }
    }

    public void prev(View v) {

        int currentItem = lesson_view.getCurrentItem();
        if (currentItem > 0)
            lesson_view.setCurrentItem(currentItem - 1, true);

    }
}