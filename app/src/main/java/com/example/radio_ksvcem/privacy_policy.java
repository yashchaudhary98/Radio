package com.example.radio_ksvcem;

import android.os.Bundle;

import com.example.radio_ksvcem.databinding.ActivityPrivacyPolicyBinding;

public class privacy_policy extends drawerBase {

    ActivityPrivacyPolicyBinding activityPrivacyPolicyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPrivacyPolicyBinding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(activityPrivacyPolicyBinding.getRoot());
        allocateActivityTitle("Privacy Policy");
    }
}