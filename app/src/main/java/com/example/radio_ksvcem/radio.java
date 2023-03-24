package com.example.radio_ksvcem;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class radio extends drawerBase{

    ActivityRadioBinding activityRadioBinding;

    MediaPlayer mediaPlayer;
    ImageView playbtn;
    TextView live;
    SeekBar seekprog;
    ImageSlider mainslide;
    LottieAnimationView animation1, animation2;

    private boolean prepared;
    private static final String RADIO_STATION_URL = "https://cast4.asurahosting.com/proxy/aryan/stream";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activityRadioBinding = ActivityRadioBinding.inflate(getLayoutInflater());
        setContentView(activityRadioBinding.getRoot());
        allocateActivityTitle("Radio");

        playbtn = activityRadioBinding.play;
        seekprog = activityRadioBinding.seekbar;
        animation1 = activityRadioBinding.animation1View;
        animation2 = activityRadioBinding.animation2View;
        live = activityRadioBinding.radioLive;
        mainslide = activityRadioBinding.imageSlider;

        final List<SlideModel> images = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Slider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren())
                            images.add(new SlideModel(Objects.requireNonNull(data.child("url").getValue()).toString(), ScaleTypes.FIT));

                        mainslide.setImageList(images,ScaleTypes.FIT);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        mediaPlayer = MediaPlayerSingleton.getInstance();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(RADIO_STATION_URL);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                prepared = true;
            }
        });

    }


    class PlayerTask implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener {
        private boolean isLive = false;
        private ProgressDialog progressDialog;

        private final Executor executor;

        public PlayerTask(){
            executor = Executors.newSingleThreadExecutor();
        }

        public void execute(String... strings){
            executor.execute(() -> {
                doInBackground(strings);
            });
        }

        private void showProgressDialog() {
            runOnUiThread(() -> {
                progressDialog = ProgressDialog.show(radio.this, "","Loading. Please wait...", true);
            });
        }

        private void dismissProgressDialog() {
            runOnUiThread(() -> {
                if(progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            });
        }


        protected Boolean doInBackground(String... strings) {

            // Check for internet connectivity
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnected()) {
            // Internet not available, dismiss the dialog bar
                dismissProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(radio.this, "Please connect to the internet", Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }
            try {
                showProgressDialog();
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
            dismissProgressDialog();
            runOnUiThread(() -> {
                Toast.makeText(radio.this, "Something went wrong. Please try again later", Toast.LENGTH_LONG).show();
            });
            return false;
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {

            dismissProgressDialog();
            playbtn.setEnabled(true);
            prepared = true;
            live.setText("Radio is Live");

            Animation animation = AnimationUtils.loadAnimation(radio.this, R.anim.text_animation);
            live.setAnimation(animation);
            playbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //If mediaplayer is in an buffering state.
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        seekprog.setProgress(0);
                        seekprog.setEnabled(false);
                        playbtn.setImageResource(R.drawable.play);
                        animation1.pauseAnimation();
                        animation2.pauseAnimation();
                        live.setVisibility(View.VISIBLE);


                    } else {
                        mediaPlayer.start();
                        seekprog.setProgress(100);
                        seekprog.setEnabled(false);
                        playbtn.setImageResource(R.drawable.pause);
                        animation1.playAnimation();
                        animation2.playAnimation();
                        live.setVisibility(View.VISIBLE);
                    }



                }
            });

        }


        @Override
        public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                // buffering started, stop playing and show a spinner or something
                progressDialog = ProgressDialog.show(radio.this, "", "Buffering. Please wait...", true);
                return true;
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                // buffering ended, start playing again and hide the spinner
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                return true;
            }
            return false;
        }
    }
}

