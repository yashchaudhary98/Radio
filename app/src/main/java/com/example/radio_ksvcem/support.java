package com.example.radio_ksvcem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class support extends AppCompatActivity {

    private boolean isbackPressed = false;

    @Override
    public void onBackPressed() {
        if(isbackPressed) {
            super.onBackPressed();
            return;
        }
        isbackPressed = true;
        Intent intent = new Intent(support.this, radio.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        getSupportActionBar().hide();

    }
}