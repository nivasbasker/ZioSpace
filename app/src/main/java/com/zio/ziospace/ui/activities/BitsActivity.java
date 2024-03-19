package com.zio.ziospace.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zio.ziospace.adapter.AdapterBits;
import com.zio.ziospace.data.ModelZBit;
import com.zio.ziospace.data.ModelZBitTopic;
import com.zio.ziospace.databinding.ActivityBitsBinding;

import java.util.ArrayList;

public class BitsActivity extends AppCompatActivity {

    ActivityBitsBinding binding;
    RecyclerView list_view;
    private AdapterBits adapter;
    private ArrayList<ModelZBitTopic> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBitsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list_view = binding.topicslist;
        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setStackFromEnd(true);
        list_view.setLayoutManager(ll);

        getPyBits();
    }

    public void getPyBits() {
        list = new ArrayList<>();
        FirebaseApp.initializeApp(this); // Initialize Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Z-bits").document("py");

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ModelZBit pyBits = documentSnapshot.toObject(ModelZBit.class);
                    list = pyBits.getTopics();
                    setView();
                } else {
                    // Document doesn't exist
                    Log.d("logger", "wrong");
                }

            }
        });
    }

    private void setView() {
        adapter = new AdapterBits(this, list);
        list_view.setAdapter(adapter);
    }
}