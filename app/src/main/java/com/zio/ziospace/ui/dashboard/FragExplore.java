package com.zio.ziospace.ui.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zio.ziospace.common.AdapterCourses;
import com.zio.ziospace.datamodels.ModelCourse;
import com.zio.ziospace.databinding.DashboardExploreBinding;

import java.util.ArrayList;


public class FragExplore extends Fragment {

    DashboardExploreBinding binding;
    RecyclerView CourseRecyclerView;
    ArrayList<ModelCourse> courseList = new ArrayList<>();
    AdapterCourses courseAdapter;
    boolean loaded = false;

    public FragExplore() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!loaded) populateList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DashboardExploreBinding.inflate(inflater, container, false);
        // Get a reference to the root view
        View rootView = binding.getRoot();

        CourseRecyclerView = binding.courseview;
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(LinearLayoutManager.HORIZONTAL);
        CourseRecyclerView.setLayoutManager(ll);

        setView();

        return rootView;
    }

    private void setView() {
        courseAdapter = new AdapterCourses(getActivity(), courseList, 1);
        CourseRecyclerView.setAdapter(courseAdapter);
    }


    private void populateList() {

        DatabaseReference courses_ref = FirebaseDatabase.getInstance().getReference("courses");
        courses_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    if (ds.child("visible").getValue(boolean.class)) {

                        ModelCourse modelCourse = ds.getValue(ModelCourse.class);
                        courseList.add(modelCourse);
                    }
                }
                loaded = true;
                setView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

}