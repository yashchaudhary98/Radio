package com.example.radio_ksvcem;


import android.content.ActivityNotFoundException;
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

public class drawerBase extends AppCompatActivity {

    public static String PACKAGE_NAME = null;
    DrawerLayout drawerLayout;

    private boolean isBackPressed = false;


    @Override
    public void setContentView(View view) {
        PACKAGE_NAME = getApplicationContext().getPackageName();

        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.navigation_side_nav);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.invisible);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

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

                    case R.id.share_nav:

                        // Code for sharing the application with others

                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        String str = "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME;
                        intent.putExtra("android.intent.extra.SUBJECT", str);
                        intent.putExtra("android.intent.extra.TEXT", str);
                        startActivity(Intent.createChooser(intent, "Share using"));
                        break;
                }
                return false;
            }
        });


        // Side Navigation code

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item){

            drawerLayout.closeDrawer(GravityCompat.START);
            switch (item.getItemId()) {

                case R.id.home_side_nav:
                    Intent intent = new Intent(getApplicationContext(),radio.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                case R.id.about_us:
                    startActivity(new Intent(getApplicationContext(), about_us.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.privacy_side_nav:
                    startActivity(new Intent(getApplicationContext(), privacy_policy.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.gallery_side_nav:
                    startActivity(new Intent(getApplicationContext(), gallery.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.Review_us_side_nav:
                    // Code for the rating of the application.
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + PACKAGE_NAME)));
                    }
                    break;

                case R.id.support_nav:
                    startActivity(new Intent(getApplicationContext(), support.class));
                    overridePendingTransition(0, 0);
                    break;
            }
            return false;
        }
        });


    }
    protected void allocateActivityTitle (String titleString){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }
}