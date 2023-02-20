package com.example.radio_ksvcem;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.radio_ksvcem.databinding.ActivityPrivacyPolicyBinding;

public class privacy_policy extends drawerBase {

    WebView webView;


    ActivityPrivacyPolicyBinding activityPrivacyPolicyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPrivacyPolicyBinding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(activityPrivacyPolicyBinding.getRoot());
        allocateActivityTitle("Privacy Policy");
        webView = findViewById(R.id.privacy_webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.freeprivacypolicy.com/live/d500f8fb-bb45-497a-b52d-c335d6caa76a\n");
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