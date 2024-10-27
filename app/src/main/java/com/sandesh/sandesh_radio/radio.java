package com.sandesh.sandesh_radio;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.sandesh_radio.R;
import com.example.sandesh_radio.databinding.ActivityRadioBinding;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class radio extends drawerBase{

    ActivityRadioBinding activityRadioBinding;

    private MediaPlayer mediaPlayer;
    private ImageView playbtn;
    private TextView live;
    private SeekBar seekprog;
    private ImageSlider mainslide;
    private LottieAnimationView animation1, animation2;

    private TelephonyManager telephonyManager;

    private AudioManager audioManager;

    private boolean prepared;
    private static final String RADIO_STATION_URL = "https://cast4.asurahosting.com/proxy/kunwarsa/stream";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activityRadioBinding = ActivityRadioBinding.inflate(getLayoutInflater());
        setContentView(activityRadioBinding.getRoot());
        allocateActivityTitle("Radio");

        FirebaseAnalytics.getInstance(this).logEvent("radio", null);
        FirebaseInAppMessaging.getInstance().setAutomaticDataCollectionEnabled(true);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mediaPlayer.setAudioAttributes(audioAttributes);
        mediaPlayer.setOnPreparedListener(mediaPlayer -> prepared = true);

        new PlayerTask().execute(RADIO_STATION_URL);

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
                        for (DataSnapshot data : dataSnapshot.getChildren())
                            images.add(new SlideModel(Objects.requireNonNull(data.child("url").getValue()).toString(), ScaleTypes.FIT));

                        mainslide.setImageList(images, ScaleTypes.FIT);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }



    class PlayerTask implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener{
        private final boolean isLive = false;
        private ProgressDialog progressDialog;

        private final Executor executor;

        public PlayerTask(){
            executor = Executors.newSingleThreadExecutor();
        }

        public void execute(String... strings){
            executor.execute(() -> doInBackground(strings));
        }

        private void showProgressDialog() {
            runOnUiThread(() -> progressDialog = ProgressDialog.show(radio.this, "","Loading. Please wait...", true));
        }

        private void dismissProgressDialog() {
            runOnUiThread(() -> {
                if(progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            });
        }


        protected Boolean doInBackground(String... strings) {

            boolean isConnectionLost = false;
            while(!isConnectionLost) {
                // Check for internet connectivity
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork == null || !activeNetwork.isConnected()) {
                    // Internet not available, dismiss the dialog bar
                    dismissProgressDialog();
                    runOnUiThread(() -> Toast.makeText(radio.this, "Please connect to the internet", Toast.LENGTH_LONG).show());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        showProgressDialog();
                        mediaPlayer.setDataSource(strings[0]);
                        mediaPlayer.setOnErrorListener(this);
                        mediaPlayer.setOnPreparedListener(this);
                        mediaPlayer.setOnInfoListener(this);
                        mediaPlayer.prepareAsync();
                        prepared = true;
                        isConnectionLost = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            dismissProgressDialog();
            mediaPlayer.release();
            runOnUiThread(() -> Toast.makeText(radio.this, "Something went wrong. Please try again later", Toast.LENGTH_LONG).show());
            prepared = false;
            return false;
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {

            dismissProgressDialog();
            playbtn.setEnabled(true);
            prepared = true;
            live.setText(R.string.radioline);

            Animation animation = AnimationUtils.loadAnimation(radio.this, R.anim.text_animation);
            live.setAnimation(animation);
            playbtn.setOnClickListener(view -> {

                //If mediaplayer is in an buffering state.
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    seekprog.setProgress(0);
                    playbtn.setImageResource(R.drawable.play);
                    animation1.cancelAnimation();
                    animation1.setProgress(0f);
                    animation2.cancelAnimation();
                    animation2.setProgress(0f);
                    live.setVisibility(View.VISIBLE);


                } else {
                    mediaPlayer.start();
                    seekprog.setProgress(100);
                    playbtn.setImageResource(R.drawable.pause);
                    animation1.playAnimation();
                    animation2.playAnimation();
                    live.setVisibility(View.VISIBLE);
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


        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if(prepared){
                int progress = (int) ((float) percent / 100 * mediaPlayer.getDuration());
                seekprog.setSecondaryProgress(progress);

                if (percent < 100) {
                    progressDialog = ProgressDialog.show(radio.this, "", "Buffering. Please wait...", true);
                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mediaPlayer != null){
            outState.putInt("currentPosition", mediaPlayer.getCurrentPosition());
            outState.putBoolean("isPlaying", mediaPlayer.isPlaying());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            int currentPosition = savedInstanceState.getInt("currentPosition", 0);
            boolean isPlaying = savedInstanceState.getBoolean("isPlaying", false);
            if(mediaPlayer != null && prepared){
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(RADIO_STATION_URL);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.seekTo(currentPosition);
                    if(isPlaying){
                        mediaPlayer.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch(state) {
                case TelephonyManager.CALL_STATE_RINGING:
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    pauseAudioPlayback();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    resumeAudioPlayback();
                    break;
            }
        }
    };

    private void resumeAudioPlayback() {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

    private void pauseAudioPlayback() {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        animation1.pauseAnimation();
        animation2.pauseAnimation();
        seekprog.setProgress(0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            animation1.resumeAnimation();
            animation2.resumeAnimation();
            seekprog.setProgress(100);
        }

    }
}

