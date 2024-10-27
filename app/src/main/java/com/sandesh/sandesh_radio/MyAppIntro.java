package com.sandesh.sandesh_radio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sandesh_radio.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class MyAppIntro extends AppIntro {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance("Welcome To Sandesh App","Get ready for an amazing radio experience with Sandesh Radio! With 24x7 live shows and daily programs, you're in for a treat!",
                R.drawable.sandesh, ContextCompat.getColor(getApplicationContext(), R.color.first)));

        addSlide(AppIntroFragment.newInstance("Enjoy Our Live Radio","Let your voice be heard and join the conversation on Sandesh Radio! Share your thoughts with us and let's celebrate the Indian culture together!",
                R.drawable.on_air_slide, ContextCompat.getColor(getApplicationContext(), R.color.second)));

        addSlide(AppIntroFragment.newInstance("Connect With Us","Need help with Sandesh Radio? Our dedicated support team is just a click away! Don't hesitate to reach out to us for any query or issue - we're here to help!",
                R.drawable.support_slide, ContextCompat.getColor(getApplicationContext(), R.color.third)));
        setFadeAnimation();

        sharedPreferences = getApplicationContext().getSharedPreferences("MyIntro", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(sharedPreferences != null){
            boolean checkedShared = sharedPreferences.getBoolean("checkedState", false);

            if(checkedShared){
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