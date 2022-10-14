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

//    DrawerLayout drawerLayout;
//    NavigationView navigationView;
//    MaterialToolbar toolbar;


//    private boolean isbackPressed = false;

    boolean prepared = false;

    String stream = "http://ksvcempro.out.airtime.pro:8000/ksvcempro_b?_ga=2.183410854.1420599539.1661599353-720114763.1661599353&_gac=1.154210122.1661599353.CjwKCAjwgaeYBhBAEiwAvMgp2guW_wq5qKZIWMyEMZRLJUc8FnhBJg1aG7Y7qQQFMr5q_BAA-ruPaxoCj_cQAvD_BwE";




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

        new PlayerTask().execute(stream);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                playbtn.setEnabled(true);
            }
        });



    }




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

            playbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        seekprog.setProgress(0);
                        playbtn.setImageResource(R.drawable.play);
                        animation1.pauseAnimation();
                        animation2.pauseAnimation();

                    }
                    else{
                        mediaPlayer.start();
                        seekprog.setProgress(100);
                        playbtn.setImageResource(R.drawable.pause);
                        animation1.playAnimation();
                        animation2.playAnimation();
                    }
                }
            });


        }



    }
}

