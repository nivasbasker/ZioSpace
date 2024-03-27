package com.zio.ziospace.ui.mycourse;

import android.os.Bundle;
import android.util.Log;
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
import com.zio.ziospace.helpers.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FragResource extends Fragment {

    RecyclerView ResourcesView;
    List<String> ResourcesList;

    String CID;

    public FragResource(String id) {
        CID = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.course_fragresources, container, false);

        ResourcesView = root.findViewById(R.id.mrview);
        LinearLayoutManager ll = new LinearLayoutManager(getContext());
        ll.setStackFromEnd(true);
        ll.setReverseLayout(true);
        ResourcesView.setLayoutManager(ll);

        load_avail_res();

        return root;
    }

    private void load_avail_res() {
        ResourcesList = new ArrayList<>();

        File local = new File(getContext().getFilesDir(), Constants.APP_FILE_RES);
        local.mkdir();
        File finallocal = new File(local, CID);
        finallocal.mkdir();


        DatabaseReference res_ref = FirebaseDatabase.getInstance().getReference("materials").child(CID);

        res_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ResourcesList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("avail").exists()) {
                        String modelmat = ds.child("title").getValue(String.class);
                        ResourcesList.add(modelmat);
                    } else {
                        String modelmat = ds.getKey();
                        ResourcesList.add(modelmat);
                    }
                }
                ResourcesView.setAdapter(new AdapterResources(getContext(), ResourcesList, CID, finallocal));
                Log.d(Constants.LOG_TAG, String.valueOf(ResourcesList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

}