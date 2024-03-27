package com.zio.ziospace.ui.mycourse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zio.ziospace.R;
import com.zio.ziospace.datamodels.ModelTask;

import java.util.ArrayList;
import java.util.List;


public class FragTasks extends Fragment {
    String CID;
    RecyclerView rview;
    List<String> title_list;
    List<Integer> types;

    public FragTasks(String cid) {
        CID = cid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.course_fragexe, container, false);

        rview = root.findViewById(R.id.proj_view);
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        rview.setLayoutManager(ll);

        loadprojs();

        return root;
    }

    private void loadprojs() {

        List<ModelTask> taskList = new ArrayList<>();

        DatabaseReference all_ex_ref = FirebaseDatabase.getInstance().getReference("tasks").child(CID);
        all_ex_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    taskList.add(ds.getValue(ModelTask.class));
                }

                rview.setAdapter(new AdapterTasks(getActivity(), taskList, CID));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


}