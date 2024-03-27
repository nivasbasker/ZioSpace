package com.zio.ziospace.ui.zbits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.zio.ziospace.R;
import com.zio.ziospace.datamodels.ModelZBitLesson;

import java.util.ArrayList;

public class AdapterLessons extends PagerAdapter {

    Context context;
    ArrayList<ModelZBitLesson> list;

    public AdapterLessons(Context context, ArrayList<ModelZBitLesson> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_lessons, container, false);

        TextView title = view.findViewById(R.id.htitle);
        TextView content = view.findViewById(R.id.hdesc);

        title.setText(list.get(position).getHeading());
        content.setText(list.get(position).getContent());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
