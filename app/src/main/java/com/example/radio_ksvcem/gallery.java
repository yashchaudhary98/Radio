package com.example.radio_ksvcem;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.radio_ksvcem.databinding.ActivityGalleryBinding;

public class gallery extends drawerBase{

    ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9, image10;
    ActivityGalleryBinding activityGalleryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGalleryBinding = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(activityGalleryBinding.getRoot());
        allocateActivityTitle("Gallery");

        image1 = activityGalleryBinding.img1;
        image2 = activityGalleryBinding.img2;
        image3 = activityGalleryBinding.img3;
        image4 = activityGalleryBinding.img4;
        image5 = activityGalleryBinding.img5;
        image6 = activityGalleryBinding.img6;
        image7 = activityGalleryBinding.img7;
        image8 = activityGalleryBinding.img8;
        image9 = activityGalleryBinding.img9;
        image10 = activityGalleryBinding.img10;


        Glide.with(this).load(R.raw.r1).into(image1);
        Glide.with(this).load(R.raw.r2).into(image2);
        Glide.with(this).load(R.raw.r3).into(image3);
        Glide.with(this).load(R.raw.r4).into(image4);
        Glide.with(this).load(R.raw.r5).into(image5);
        Glide.with(this).load(R.raw.r6).into(image6);
        Glide.with(this).load(R.raw.r7).into(image7);
        Glide.with(this).load(R.raw.r8).into(image8);
        Glide.with(this).load(R.raw.r9).into(image9);
        Glide.with(this).load(R.raw.r10).into(image10);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}