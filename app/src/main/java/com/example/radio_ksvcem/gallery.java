package com.example.radio_ksvcem;

import android.os.Bundle;

import com.example.radio_ksvcem.databinding.ActivityGalleryBinding;

public class gallery extends drawerBase{
    ActivityGalleryBinding activityGalleryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGalleryBinding = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(activityGalleryBinding.getRoot());
        allocateActivityTitle("Gallery");
    }
}