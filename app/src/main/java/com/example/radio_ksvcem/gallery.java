package com.example.radio_ksvcem;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.radio_ksvcem.databinding.ActivityGalleryBinding;

public class gallery extends drawerBase{

    ImageView image1, image2, image3, image4, image5;
    ActivityGalleryBinding activityGalleryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGalleryBinding = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(activityGalleryBinding.getRoot());
        allocateActivityTitle("Gallery");

        image1 = findViewById(R.id.img1);
        image2 = findViewById(R.id.img2);
        image3 = findViewById(R.id.img3);
        image4 = findViewById(R.id.img4);
        image5 = findViewById(R.id.img5);

        Glide.with(this).load(R.raw.r1).into(image1);
        Glide.with(this).load(R.raw.r2).into(image2);
        Glide.with(this).load(R.raw.r3).into(image3);
        Glide.with(this).load(R.raw.r4).into(image4);
        Glide.with(this).load(R.raw.r5).into(image5);

    }
}