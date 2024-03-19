package com.zio.ziospace.components;

import static com.zio.ziospace.helpers.Constants.LOG_TAG;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zio.ziospace.R;

import android.icu.util.Calendar;

import androidx.annotation.NonNull;

import java.util.List;

public class AdapterDays extends BaseAdapter {

    private final Context context;
    private final int month, lastdate, year;
    private int date = 1;

    private final Calendar today;


    List<Calendar> events;


    ClickListener listener = null;

    public interface ClickListener {
        void OnClickListener(int position);
    }

    public AdapterDays(Context context, int month, int year, @NonNull List<Calendar> events, ClickListener listener) {
        this.context = context;
        this.month = month;
        this.listener = listener;
        this.events = events;
        this.year = year;


        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month + 1, 1);
        calendar.add(Calendar.DATE, -1);
        lastdate = calendar.get(Calendar.DATE);

        today = Calendar.getInstance();


    }

    @Override
    public int getCount() {
        return 35;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_day, viewGroup, false);
            ((TextView) view).setGravity(Gravity.CENTER);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int pos = (i + 2) % 7;
        if (pos == 0) pos = 7;

        if (dayOfWeek == pos && date <= lastdate) {
            ((TextView) view).setText(String.valueOf(date));
            date++;

            if (events != null) {
                for (int ind = 0; ind < events.size(); ind++) {
                    Calendar eventday = events.get(ind);
                    if (isSameDate(eventday, calendar)) {
                        Log.d(LOG_TAG, "Match");
                        int finalInd = ind;
                        if (eventday.before(today)) {
                            view.setBackgroundColor(context.getResources().getColor(R.color.grey));
                            if (listener != null)
                                view.setOnClickListener(view13 -> listener.OnClickListener(finalInd));
                        } else if (isSameDate(today, calendar)) {
                            view.setBackgroundColor(context.getResources().getColor(R.color.blue));
                            if (listener != null)
                                view.setOnClickListener(view1 -> listener.OnClickListener(finalInd));
                        } else {
                            view.setBackgroundColor(context.getResources().getColor(R.color.green));
                            if (listener != null)
                                view.setOnClickListener(view12 -> listener.OnClickListener(finalInd));
                        }
                    }
                }
            }
        }

        return view;

    }

    private boolean isSameDate(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

}
