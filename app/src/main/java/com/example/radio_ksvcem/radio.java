package com.example.radio_ksvcem;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.radio_ksvcem.databinding.ActivityRadioBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class radio extends drawerBase{

    ActivityRadioBinding activityRadioBinding;

    MediaPlayer mediaPlayer;
    ImageView playbtn;
    SeekBar seekprog;
    ImageSlider mainslide;
    Handler handler = new Handler();
    LottieAnimationView animation1, animation2;

    private boolean prepared;
    private byte[] buffer;

    private static final String RADIO_STATION_URL = "https://ksvcem.out.airtime.pro/ksvcem_a?_ga=2.78166676.1819775058.1676160883-927249201.1676160883&_gac=1.54764121.1676160883.CjwKCAiAlp2fBhBPEiwA2Q10D7OyUuB4ew6IvrA98WKmUIalpJMEGtJ7SW3Ui4HYhQm-PZdv8f_eIxoC4bkQAvD_BwE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activityRadioBinding = ActivityRadioBinding.inflate(getLayoutInflater());
        setContentView(activityRadioBinding.getRoot());
        allocateActivityTitle("Radio");

        playbtn = findViewById(R.id.play);
        seekprog = findViewById(R.id.seekbar);
        animation1 = findViewById(R.id.animation1_view);
        animation2 = findViewById(R.id.animation2_view);




        mainslide = findViewById(R.id.image_slider);

        final List<SlideModel> images = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Slider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren())
                            images.add(new SlideModel(data.child("url").getValue().toString(), ScaleTypes.FIT));

                        mainslide.setImageList(images,ScaleTypes.FIT);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(RADIO_STATION_URL);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                playbtn.setEnabled(true);
                prepared = true;
            }
        });


    }


    class PlayerTask extends AsyncTask<String, Void, Boolean> implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener {
        private boolean isBuffering = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setOnInfoListener(this);
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

            playbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //If mediaplayer is in an buffering state.
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        seekprog.setProgress(0);
                        playbtn.setImageResource(R.drawable.play);
                        animation1.pauseAnimation();
                        animation2.pauseAnimation();


                    } else {
                        mediaPlayer.start();
                        seekprog.setProgress(100);
                        playbtn.setImageResource(R.drawable.pause);
                        animation1.playAnimation();
                        animation2.playAnimation();
                    }



                }
            });

        }

        @Override
        public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                // buffering started, stop playing and show a spinner or something
                mediaPlayer.pause();
                isBuffering = true;
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                // buffering ended, start playing again and hide the spinner
                mediaPlayer.start();
                isBuffering = false;
            }
            return true;
        }
    }
}

