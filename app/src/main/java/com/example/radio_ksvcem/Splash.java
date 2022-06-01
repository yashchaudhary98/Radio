package com.example.radio_ksvcem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    private TextView radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        radio = findViewById(R.id.radioText);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash.this, radio.class);
            startActivity(intent);
            finish();
        },9000);

        Animation animation = AnimationUtils.loadAnimation(Splash.this, R.anim.animation);
        radio.startAnimation(animation);
    }
}