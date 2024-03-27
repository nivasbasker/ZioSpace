package com.zio.ziospace.customviews.calenderview;

import static android.icu.util.Calendar.MONTH;
import static android.icu.util.Calendar.YEAR;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.util.Calendar;

import java.util.List;

public class CustomCalendarView extends RecyclerView {

    private Paint paint;
    private final Context context;

    List<Calendar> events;

    ClickListener listener = null;

    public interface ClickListener {
        void OnClickListener(int position);
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public CustomCalendarView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(layoutManager);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(this);

        Calendar calendar = Calendar.getInstance();

        // Extract day, month, and year
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based, so add 1
        int year = calendar.get(Calendar.YEAR);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //this.setAdapter(new AdapterMonths(context));

    }


    public void setEvents(List<Calendar> events) {
        if (events == null || events.size() < 1) return;

        this.events = events;
        this.setAdapter(new AdapterMonths(context, events, new AdapterMonths.ClickListener() {
            @Override
            public void OnClickListener(int position) {
                if (listener != null)
                    listener.OnClickListener(position);
            }
        }));

        int start = events.get(0).get(MONTH);
        int start_year = events.get(0).get(YEAR);
        int end = Calendar.getInstance().get(MONTH);
        int end_year = Calendar.getInstance().get(YEAR);

        smoothScrollToPosition(end - start + (12 * (end_year - start_year)));
    }


}
