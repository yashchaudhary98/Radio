package com.sandesh.sandesh_radio;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sandesh_radio.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class drawerBase extends AppCompatActivity {

    public static String PACKAGE_NAME = null;
    DrawerLayout drawerLayout;

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


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.whatsapp:
                    String url = "https://wa.me/+918960258960";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    break;

                case R.id.instagram:
                    Intent t = new Intent(Intent.ACTION_VIEW);
                    t.setData(Uri.parse("https://instagram.com/radiosandeshbijnor"));
                    startActivity(t);
                    break;

                case R.id.facebook:
                    Intent k = new Intent(Intent.ACTION_VIEW);
                    k.setData(Uri.parse("https://www.facebook.com/radiosandeshbijnor?mibextid=ZbWKwL"));
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
        });


        // Side Navigation code

        navigationView.setNavigationItemSelectedListener(item -> {

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
        });


    }
    protected void allocateActivityTitle (String titleString){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }
    @SuppressLint("IntentReset")
    public void composeEmail() {

        Log.d(TAG, "email_us");

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"radiosandesh89.6@ksvira.edu.in"});

        try {

            startActivity(Intent.createChooser(intent, "choose any below application"));
        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(getApplicationContext(), "No supported application installed", Toast.LENGTH_LONG).show();
        }

    }

}