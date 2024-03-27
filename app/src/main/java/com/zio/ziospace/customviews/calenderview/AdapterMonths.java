package com.zio.ziospace.customviews.calenderview;

import static android.icu.util.Calendar.MONTH;
import static android.icu.util.Calendar.YEAR;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zio.ziospace.R;

import java.util.List;

import android.icu.util.Calendar;

public class AdapterMonths extends RecyclerView.Adapter<AdapterMonths.ViewHolder> {


    private final Context context;
    private int start_month;
    private List<Calendar> events;


    ClickListener listener = null;

    public interface ClickListener {
        void OnClickListener(int position);
    }

    public AdapterMonths(Context context, @NonNull List<Calendar> events, ClickListener listener) {
        this.context = context;
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_month, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Calendar calendar = (Calendar) events.get(0).clone();
        calendar.add(MONTH, position);

        holder.monthname.setText(getName(calendar.get(MONTH)));

        holder.dayview.setAdapter(new AdapterDays(context, calendar.get(MONTH), calendar.get(YEAR), events,
                new AdapterDays.ClickListener() {
                    @Override
                    public void OnClickListener(int position) {
                        if (listener != null)
                            listener.OnClickListener(position);
                    }
                }));
    }

    @Override
    public int getItemCount() {
        if (events == null) return 1;

        int start = events.get(0).get(MONTH);
        int start_year = events.get(0).get(YEAR);
        int end = events.get(events.size() - 1).get(MONTH);
        int end_year = events.get(events.size() - 1).get(YEAR);
        return end - start + 1 + (12 * (end_year - start_year));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView monthname;
        GridView dayview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            monthname = itemView.findViewById(R.id.month_name);
            dayview = itemView.findViewById(R.id.days_view);
        }
    }


    private int getName(int now) {
        switch (now) {
            case 0:
                return R.string.jan;
            case 1:
                return R.string.feb;
            case 2:
                return R.string.mar;
            case 3:
                return R.string.apr;
            case 4:
                return R.string.may;
            case 5:
                return R.string.jun;
            case 6:
                return R.string.jul;
            case 7:
                return R.string.aug;
            case 8:
                return R.string.sep;
            case 9:
                return R.string.oct;
            case 10:
                return R.string.nov;
            case 11:
                return R.string.dec;

        }
        return R.string.unknown;
    }

}
