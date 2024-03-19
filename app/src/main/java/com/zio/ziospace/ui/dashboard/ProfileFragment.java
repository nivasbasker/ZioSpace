package com.zio.ziospace.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zio.ziospace.databinding.DashboardProfileBinding;
import com.zio.ziospace.ui.settings.BcmMentor;
import com.zio.ziospace.ui.settings.SettingsActivity;
import com.zio.ziospace.helpers.LoginManager;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private DashboardProfileBinding binding;
    private String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DashboardProfileBinding.inflate(inflater, container, false);
        username = LoginManager.getUserName(getContext());
        binding.tvs.setText(username);


        binding.openSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });
        binding.mentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BcmMentor.class));
            }
        });
        return binding.getRoot();
    }
}