package com.example.radio_ksvcem;

import android.os.Bundle;
import android.view.View;
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
        webView = activityPrivacyPolicyBinding.privacyWebview;
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.freeprivacypolicy.com/live/d500f8fb-bb45-497a-b52d-c335d6caa76a\n");
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