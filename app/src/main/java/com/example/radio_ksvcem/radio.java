package com.example.radio_ksvcem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;

public class radio extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    TextView playbtn, pausebtn;
    SeekBar seekprog;
    Handler handler = new Handler();
    LottieAnimationView animation;
    TextView facebook, instagram, support, twitter;
    private boolean isbackPressed = false;


    @Override
    public void onBackPressed() {
        if(isbackPressed){
            super.onBackPressed();
            return;
        }
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG).show();
        isbackPressed = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isbackPressed = false;
            }
        },2000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        Objects.requireNonNull(getSupportActionBar()).hide();

        playbtn = findViewById(R.id.play);
        pausebtn = findViewById(R.id.pause);
        seekprog = findViewById(R.id.seekbar);
        animation = findViewById(R.id.animation1_view);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        twitter = findViewById(R.id.twitter);
        support = findViewById(R.id.support);

//        seekprog.setMax(mediaPlayer.getDuration());

        seekprog.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               if(fromUser) {
                   mediaPlayer.seekTo(progress);
               }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = instagram.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.instagram.com/"));
                startActivity(i);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = facebook.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.facebook.com/login/"));
                startActivity(i);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = twitter.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://twitter.com/ksvcem"));
                startActivity(i);
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(radio.this, support.class);
                startActivity(intent);
            }
        });

    }

    public class updateSeekBar implements Runnable {

        @Override
        public void run() {
            seekprog.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(this, 100);
        }
    }


    public void music(View view) {

        switch(view.getId()) {
            case R.id.play:
                if(mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.freesmoke);

                    updateSeekBar updateSeekBar = new updateSeekBar();

                    handler.post(updateSeekBar);
                }
                mediaPlayer.start();
                animation.playAnimation();
                break;


            case R.id.pause:
                if(mediaPlayer != null){
                    mediaPlayer.pause();
                    animation.pauseAnimation();
                }
                break;
        }
    }

}