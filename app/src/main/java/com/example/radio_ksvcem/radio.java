package com.example.radio_ksvcem;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class radio extends drawerBase{

    ActivityRadioBinding activityRadioBinding;

    MediaPlayer mediaPlayer;
    ImageView playbtn;
    TextView live;
    SeekBar seekprog;
    ImageSlider mainslide;
    Handler handler = new Handler();
    LottieAnimationView animation1, animation2;

    private boolean prepared;
    private byte[] buffer;

    private static final String RADIO_STATION_URL = "https://ksvcem1.out.airtime.pro/ksvcem1_a?_ga=2.63170347.560964941.1676879600-807033401.1676879600&_gac=1.48798164.1676879600.Cj0KCQiArsefBhCbARIsAP98hXS8OpOikG2XBRHScGv5l09Py7ZktiYv2JEHVGusb4KihX_zVn6vqbUaArO3EALw_wcB";

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
        live = findViewById(R.id.radio_live);


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
//                playbtn.setEnabled(true);
                prepared = true;
            }
        });


    }


    class PlayerTask extends AsyncTask<String, Void, Boolean> implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener {
        private boolean isBuffering = true;
        private boolean isLive = false;
        private ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(radio.this, "", "Loading. Please wait...", true);
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            // Check for internet connectivity
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnected()) {
            // Internet not available, dismiss the dialog bar
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(radio.this, "Please connect to the internet", Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                isLive = (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
                connection.disconnect();

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

            progressDialog.dismiss();
            if(isLive){
                playbtn.setEnabled(true);
                prepared = true;
                live.setText("Radio is Live");

                Animation animation = AnimationUtils.loadAnimation(radio.this, R.anim.text_animation);
                live.setAnimation(animation);

            }else {
                Toast.makeText(radio.this, "Radio station is currently offline", Toast.LENGTH_LONG).show();
                mediaPlayer.reset();
            }
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
                        live.setVisibility(View.VISIBLE);


                    } else {
                        mediaPlayer.start();
                        seekprog.setProgress(100);
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
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                // buffering ended, start playing again and hide the spinner
                progressDialog.dismiss();
            }
            return true;
        }
    }
}

