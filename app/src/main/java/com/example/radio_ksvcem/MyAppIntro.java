package com.example.radio_ksvcem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class MyAppIntro extends AppIntro {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance("Welcome To Sandesh App","Sandesh Radio is ready to provide you the amazing radio experience with 24x7 live and daily shows",
                R.drawable.mic_slide, ContextCompat.getColor(getApplicationContext(), R.color.first)));

        addSlide(AppIntroFragment.newInstance("Enjoy Our Live Radio","Listen to our radio and share your thoughts with us and ready to bring up the voice of indians",
                R.drawable.on_air_slide, ContextCompat.getColor(getApplicationContext(), R.color.second)));

        addSlide(AppIntroFragment.newInstance("Connect With Us","Support Teams are waiting for your query and issues regarding the application to help you out",
                R.drawable.support_slide, ContextCompat.getColor(getApplicationContext(), R.color.third)));
        setFadeAnimation();

        sharedPreferences = getApplicationContext().getSharedPreferences("MyIntro", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(sharedPreferences != null){
            boolean checkedShared = sharedPreferences.getBoolean("checkedState", false);

            if(checkedShared == true){
                startActivity(new Intent(getApplicationContext(), Splash.class));
                finish();
            }
        }

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), Splash.class));
        editor.putBoolean("checkedState", false).commit();
        finish();

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), Splash.class));
        editor.putBoolean("checkedState", true).commit();
        finish();
    }
}