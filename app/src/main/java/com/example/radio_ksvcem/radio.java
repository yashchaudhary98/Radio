package com.example.radio_ksvcem;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.io.IOException;
import java.util.Objects;

public class radio extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    ImageView playbtn;
    SeekBar seekprog;
    Handler handler = new Handler();
    LottieAnimationView animation1, animation2;
    TextView facebook, youtube, support, twitter;
    private boolean isbackPressed = false;

    boolean prepared = false;

    String stream = "https://www.radioking.com/play/ksvcem";


    @Override
    public void onBackPressed() {
        if (isbackPressed) {
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
        }, 2000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        Objects.requireNonNull(getSupportActionBar()).hide();

        playbtn = findViewById(R.id.play);
        seekprog = findViewById(R.id.seekbar);
        animation1 = findViewById(R.id.animation1_view);
        animation2 = findViewById(R.id.animation2_view);
        facebook = findViewById(R.id.facebook);
        youtube = findViewById(R.id.youtube);
        twitter = findViewById(R.id.twitter);
        support = findViewById(R.id.support);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(stream);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                playbtn.setEnabled(true);
            }
        });


        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    seekprog.setProgress(0);
                    playbtn.setImageResource(R.drawable.play);
                    animation1.pauseAnimation();
                    animation2.pauseAnimation();
                    Intent intent = new Intent(radio.this, radio.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0,0);

                }else {
                    mediaPlayer.start();
                    seekprog.setProgress(100);
                    playbtn.setImageResource(R.drawable.pause);
                    animation1.playAnimation();
                    animation2.playAnimation();
                }
//                else if(mediaPlayer == null){
//                    assert false;
//                    mediaPlayer.start();
//                    playbtn.setImageResource(R.drawable.pause);
//                    animation1.playAnimation();
//                    animation2.playAnimation();
//                }else{
//                    mediaPlayer.start();
//                    playbtn.setImageResource(R.drawable.pause);
//                    animation1.playAnimation();
//                    animation2.playAnimation();
//                }
            }
        });

//
//        seekprog.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if(fromUser){
//                    mediaPlayer.seekTo(progress);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.youtube.com/"));
                startActivity(i);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.facebook.com/login/"));
                startActivity(i);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


//    private void play() {
//
//        if (mediaPlayer == null) {
////            updateSeekBar updateSeekBar = new updateSeekBar();
//            seekprog.setProgress(100);
////            handler.post(updateSeekBar);
//            mediaPlayer.prepareAsync();
//            mediaPlayer.start();
//            playbtn.setImageResource(R.drawable.pause);
//            animation1.playAnimation();
//            animation2.playAnimation();
//
//        } else if (mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//            seekprog.setProgress(0);
//            playbtn.setImageResource(R.drawable.play);
//            animation1.pauseAnimation();
//            animation2.pauseAnimation();
//            Intent intent = new Intent(radio.this, radio.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//            overridePendingTransition(0, 0);
//        } else if (mediaPlayer != null) {
//            mediaPlayer.start();
//            seekprog.setProgress(100);
//            playbtn.setImageResource(R.drawable.pause);
//            animation1.playAnimation();
//            animation2.playAnimation();
////
//        }
//
//    }




    class PlayerTask extends AsyncTask<String, Void, Boolean> implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return prepared;
        }

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            return false;
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {

        }


//    public class updateSeekBar implements Runnable {
//
//        @Override
//        public void run() {
//            seekprog.setProgress(mediaPlayer.getCurrentPosition());
//            handler.postDelayed(this, 100);
//        }
//    }
    }
}

