package com.zio.ziospace.helpers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zio.ziospace.R;
import com.zio.ziospace.data.ModelUser;
import com.zio.ziospace.components.CustomCalendarView;

import java.util.ArrayList;
import java.util.List;

public class TestingActivity extends AppCompatActivity {

    ArrayList<ModelUser> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        CustomCalendarView calendarView = findViewById(R.id.calendar);
        List<Calendar> events = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 23);
        events.add((Calendar) calendar.clone());  // Clone the calendar to create a new instance
        calendar.set(2024, Calendar.MARCH, 2);
        events.add((Calendar) calendar.clone());
        calendar.set(2024, Calendar.MARCH, 9);
        events.add((Calendar) calendar.clone());


        calendarView.setClickListener(new CustomCalendarView.ClickListener() {
            @Override
            public void OnClickListener(int position) {
                Toast.makeText(TestingActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

            }
        });
        calendarView.setEvents(events);
    }

    private void load() {
        DatabaseReference courses_ref = FirebaseDatabase.getInstance().getReference("USERINFO");
        courses_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser modelAllCourse = ds.getValue(ModelUser.class);
                    list.add(modelAllCourse);
                }
                Log.e("TAG", "loaded");
                add();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void add() {
        for (ModelUser city : list) {
            // Use the state code as the document ID
            String documentId = city.getUsername();

            // Get a reference to the document
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("users").document(documentId);

            // Write the data to Firestore
            documentReference.set(city)
                    .addOnSuccessListener(aVoid -> {

                    })
                    .addOnFailureListener(e -> {
                        // Handle errors
                        Log.e("TAG", "Error adding document", e);
                    });
        }
        Log.e("TAG", "out of loop");


    }
}