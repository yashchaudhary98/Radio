package com.example.radio_ksvcem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;

import java.util.Objects;

public class radio extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    ImageView playbtn;
    SeekBar seekprog;
    Handler handler = new Handler();
    LottieAnimationView animation;
    TextView facebook, youtube, support, twitter;
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
        seekprog = findViewById(R.id.seekbar);
        animation = findViewById(R.id.animation1_view);
        facebook = findViewById(R.id.facebook);
        youtube = findViewById(R.id.youtube);
        twitter = findViewById(R.id.twitter);
        support = findViewById(R.id.support);

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });



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

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = youtube.getText().toString();
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

    public void play(){
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.freesmoke);
            playbtn.setImageResource(R.drawable.pause);

            updateSeekBar updateSeekBar = new updateSeekBar();

            handler.post(updateSeekBar);

            mediaPlayer.start();
            animation.playAnimation();

        }else if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                playbtn.setImageResource(R.drawable.play);
                animation.pauseAnimation();
//            pausebtn.setImageResource(R.drawable.play);
            }else if(mediaPlayer != null){
            mediaPlayer.start();
            playbtn.setImageResource(R.drawable.pause);
            animation.playAnimation();
        }
        }
}


//    public void pause(){
//        if(mediaPlayer != null){
//            mediaPlayer.pause();
//            animation.pauseAnimation();
////            pausebtn.setImageResource(R.drawable.play);
//        }
//    }


//    public void music(View view) {
//
//        switch(view.getId()) {
//            case R.id.play:
//                if(mediaPlayer == null) {
//                    mediaPlayer = MediaPlayer.create(this, R.raw.freesmoke);
//                    playbtn.setImageResource(R.drawable.pause);
//
//                    updateSeekBar updateSeekBar = new updateSeekBar();
//
//                    handler.post(updateSeekBar);
//
//                    mediaPlayer.start();
//                    animation.playAnimation();
//                }
//
//                break;
//
//
//            case R.id.pause:
//                if(mediaPlayer != null){
//                    mediaPlayer.pause();
//                    animation.pauseAnimation();
//                    pausebtn.setImageResource(R.drawable.play);
//                }
//                break;
//        }
//    }

