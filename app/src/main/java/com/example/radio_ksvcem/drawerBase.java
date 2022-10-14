package com.example.radio_ksvcem;


import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class drawerBase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    private boolean isBackPressed = false;


    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed();
            return;
        }
        isBackPressed = true;
//        Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG).show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isBackPressed = false;
//            }
//        }, 2000);
    }

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base,null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.navigation_side_nav);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.home_nav:
                        startActivity(new Intent(drawerBase.this, radio.class));
                        overridePendingTransition(0,0);
                        break;

                    case R.id.website_nav:
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://ksvira.edu.in/vira_home.php"));
                        startActivity(i);
                        break;
                    case R.id.tweet_nav:
                        Intent j = new Intent(Intent.ACTION_VIEW);
                        j.setData(Uri.parse("https://twitter.com/ksvcem"));
                        startActivity(j);
                        break;
                    case R.id.youtube_nav:
                        Intent k = new Intent(Intent.ACTION_VIEW);
                        k.setData(Uri.parse("https://www.youtube.com/"));
                        startActivity(k);
                        break;
                    case R.id.support_nav:
                        startActivity(new Intent(drawerBase.this, support.class));
                        overridePendingTransition(0,0);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                return true;
            }
        });

    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){

            case R.id.home_side_nav:
                startActivity(new Intent(this,radio.class));
                overridePendingTransition(0,0);
                break;
            case R.id.about_us:
                startActivity(new Intent(this, about_us.class));
                overridePendingTransition(0,0);
                break;
            case R.id.privacy_side_nav:
                startActivity(new Intent(this,privacy_policy.class));
                overridePendingTransition(0,0);
                break;
            case R.id.podcast_side_nav:
                startActivity(new Intent(this,podcast_page.class));
                overridePendingTransition(0,0);
                break;
            case R.id.gallery_side_nav:
                startActivity(new Intent(this, gallery.class));
                overridePendingTransition(0,0);
                break;
            case R.id.Review_us_side_nav:
                startActivity(new Intent(this,support.class));
                overridePendingTransition(0,0);
                break;
        }
        return true;
    }

    protected void allocateActivityTitle(String titleString){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }
    }
}