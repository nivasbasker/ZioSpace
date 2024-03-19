package com.zio.ziospace.ui.course;


import static com.zio.ziospace.helpers.Constants.LOG_TAG;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zio.ziospace.R;
import com.zio.ziospace.components.CustomCalendarView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FragBlocks extends Fragment {

    String CID, CLASSLINK;

    CustomCalendarView schedule;

    PopupWindow popupWindow;

    List<Calendar> lesson_list;
    List<String> topics;


    public FragBlocks(String cid, String link) {
        CID = cid;
        CLASSLINK = link;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.course_fragblocks, container, false);

        schedule = root.findViewById(R.id.calendar);
        popupWindow = new PopupWindow(LayoutInflater.from(getContext()).inflate(R.layout.popup_schedule, null),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);

        schedule.setClickListener(new CustomCalendarView.ClickListener() {
            @Override
            public void OnClickListener(int position) {
                openpop(position);
            }
        });
        loadlessons();

        return root;
    }

    private void openpop(int ind) {

        popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 50);

        TextView tv = popupWindow.getContentView().findViewById(R.id.schedule_text);
        TextView tv2 = popupWindow.getContentView().findViewById(R.id.schedule_index);
        tv.setText("Topic : " + topics.get(ind));
        tv2.setText("Day : " + (ind + 1));

        Calendar calendar = Calendar.getInstance();
        if (!lesson_list.get(ind).before(calendar)) {
            popupWindow.getContentView().findViewById(R.id.schedule_action).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "tap to join", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void loadlessons() {

        lesson_list = new ArrayList<>();
        topics = new ArrayList<>();

        DatabaseReference block_ref = FirebaseDatabase.getInstance().getReference("lessons").child(CID);
        block_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date parsedDate = dateFormat.parse(ds.child("date").getValue(String.class));

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(parsedDate);

                        lesson_list.add(calendar);
                        topics.add(ds.child("topic").getValue(String.class));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                //if (!lesson_list.isEmpty())
                    schedule.setEvents(lesson_list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "Could not Refresh, Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }


}