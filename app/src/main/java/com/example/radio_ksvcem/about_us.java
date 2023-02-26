package com.example.radio_ksvcem;

import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.radio_ksvcem.databinding.ActivityAboutUsBinding;

public class about_us extends drawerBase {

    WebView webView;

    ActivityAboutUsBinding activityAboutUsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutUsBinding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(activityAboutUsBinding.getRoot());
        allocateActivityTitle("About Us");
        webView = activityAboutUsBinding.aboutusWebview;
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://about-us-sandesh.netlify.app/");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.getSettings().setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

