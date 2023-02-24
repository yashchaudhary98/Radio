package com.example.radio_ksvcem;

import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.radio_ksvcem.databinding.ActivityAboutUsBinding;

import java.io.File;

public class about_us extends drawerBase {

    WebView webView;

    ActivityAboutUsBinding activityAboutUsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutUsBinding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(activityAboutUsBinding.getRoot());
        allocateActivityTitle("About Us");
        webView = findViewById(R.id.aboutus_webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://inspiring-tartufo-679fb9.netlify.app/");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);


        }

    @Override
    public void onBackPressed() {
      if(webView.canGoBack()){

          webView.goBack();

      }else{

          super.onBackPressed();
      }
    }
}

