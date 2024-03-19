package com.zio.ziospace.ui.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zio.ziospace.adapter.AdapterCourses;
import com.zio.ziospace.data.ModelCourse;
import com.zio.ziospace.databinding.DashboardCourseBinding;
import com.zio.ziospace.helpers.LoginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyCoursesFragment extends Fragment {

    DashboardCourseBinding binding;
    RecyclerView MyCoursesView, CompletedCourseView;
    List<ModelCourse> MycoursesList = new ArrayList<>(), CompletedCourseLists = new ArrayList<>();
    AdapterCourses mycourseadapter, CompletedCourseAdapter;
    String USERNAME;
    TextView OngoingText, CompletedText;

    boolean loaded = false;

    public MyCoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!loaded)
            populateList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DashboardCourseBinding.inflate(inflater, container, false);
        // Get a reference to the root view

        MyCoursesView = binding.mycourseRview;
        CompletedCourseView = binding.cmplCourse;
        OngoingText = binding.ongcTxt;
        CompletedText = binding.cmplTxt;

        USERNAME = LoginManager.getUserName(requireContext());

        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        MyCoursesView.setLayoutManager(ll);

        setView();

        return binding.getRoot();
    }

    private void setView() {

        if (!MycoursesList.isEmpty()) {
            mycourseadapter = new AdapterCourses(getActivity(), MycoursesList, 2);
            MyCoursesView.setAdapter(mycourseadapter);
            OngoingText.setVisibility(View.VISIBLE);
            binding.noCrs.setVisibility(View.GONE);
        }
        if (!CompletedCourseLists.isEmpty()) {
            CompletedCourseAdapter = new AdapterCourses(getActivity(), CompletedCourseLists, 3);
            CompletedCourseView.setAdapter(CompletedCourseAdapter);
            CompletedText.setVisibility(View.VISIBLE);
            binding.noCrs.setVisibility(View.GONE);
        }
    }

    private void populateList() {

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("courses");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MycoursesList.clear();
                CompletedCourseLists.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    if (ds.child("fin_users").child(USERNAME).exists()) {
                        ModelCourse modelAllCrs = ds.getValue(ModelCourse.class);
                        CompletedCourseLists.add(modelAllCrs);
                    } else if (ds.child("reg_users").child(USERNAME).exists()) {
                        ModelCourse modelCourse = ds.getValue(ModelCourse.class);
                        MycoursesList.add(modelCourse);
                    }
                }
                loaded = true;
                setView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Could not Refresh, Please try again later", Toast.LENGTH_SHORT).show();
            }

        });
    }
}