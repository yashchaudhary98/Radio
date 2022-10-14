package com.example.radio_ksvcem;

import android.os.Bundle;

import com.example.radio_ksvcem.databinding.ActivityAboutUsBinding;

public class about_us extends drawerBase {

    ActivityAboutUsBinding activityAboutUsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutUsBinding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(activityAboutUsBinding.getRoot());
        allocateActivityTitle("About Us");
    }
}