package com.example.radio_ksvcem;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class podcast_page extends drawerBase{

//    ActivityPodcastPageBinding activityPodcastPageBinding;

    ViewPager2 viewPager2;

    DatabaseReference mref;

    TextView SongName, SongArtist;

    MediaPlayer mediaPlayer2;

    boolean play = true;
    ImageView Play, Pause, Prev,Next;

    Integer currentSongIndex = 0;

    ArrayList<String> imageurls = new ArrayList<>();
    ArrayList<String> songnames = new ArrayList<>();
    ArrayList<String> songartists = new ArrayList<>();
    ArrayList<String> songurls = new ArrayList<>();
    List<SliderItems> sliderItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        activityPodcastPageBinding = ActivityPodcastPageBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_podcast_page);
//        allocateActivityTitle("Podcast");

        viewPager2 = findViewById(R.id.ViewpagerImageSlider);

        SongName = findViewById(R.id.songname);
        SongArtist = findViewById(R.id.songartist);


        Play = findViewById(R.id.podcast_play);
        Pause = findViewById(R.id.podcast_pause);
        Prev = findViewById(R.id.prev);
        Next = findViewById(R.id.next);


        mref = FirebaseDatabase.getInstance().getReference().child("Podcast");

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    imageurls.add(ds.child("imageUrl").getValue(String.class));

                    songnames.add(ds.child("songname").getValue(String.class));
                    songartists.add(ds.child("songartist").getValue(String.class));
                    songurls.add(ds.child("songUrl").getValue(String.class));
                }
                for(int i =0; i<imageurls.size();i++){
                    sliderItems.add(new SliderItems(imageurls.get(i)));
                }
                viewPager2.setAdapter(new SliderAdapter(sliderItems));
                viewPager2.setClipToPadding(false);
                viewPager2.setClipChildren(false);
                viewPager2.setOffscreenPageLimit(3);
                viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        page.setScaleY(1);
                    }
                });
                viewPager2.setPageTransformer(compositePageTransformer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                init(viewPager2.getCurrentItem());
                currentSongIndex = viewPager2.getCurrentItem();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSongIndex = currentSongIndex + 1;
                viewPager2.setCurrentItem(currentSongIndex);

                init(currentSongIndex);
            }
        });

        Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSongIndex = currentSongIndex - 1;
                viewPager2.setCurrentItem(currentSongIndex);

                init(currentSongIndex);
            }
        });


    }

    private void init(int currentItem) {

        try {
            if(mediaPlayer2.isPlaying())
                mediaPlayer2.reset();
        }catch (Exception e){

        }
        Pause.setVisibility(View.VISIBLE);
        Play.setVisibility(View.INVISIBLE);
        play = true;

        SongName.setText(songnames.get(currentItem));
        SongArtist.setText(songartists.get(currentItem));

        try {
            mediaPlayer2 = new MediaPlayer();
            mediaPlayer2.setDataSource(songurls.get(currentItem));
            mediaPlayer2.prepareAsync();
            mediaPlayer2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer2) {
                    mediaPlayer2.start();;
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void playbutton(View view) {
        play = true;
        Pause.setVisibility(View.VISIBLE);
        Play.setVisibility(View.INVISIBLE);
        mediaPlayer2.start();
    }

    public void pausebutton(View view) {
        if(play){
            play = false;
            Pause.setVisibility(View.INVISIBLE);
            Play.setVisibility(View.VISIBLE);
            mediaPlayer2.pause();
        }
    }
}