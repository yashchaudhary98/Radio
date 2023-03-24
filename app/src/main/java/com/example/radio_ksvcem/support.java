package com.example.radio_ksvcem;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radio_ksvcem.databinding.ActivitySupportBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class support extends drawerBase{
    private boolean isBackPressed = false;
    EditText name, email, msg;
    TextView sendbtn, call, email_us;
    ProgressDialog progressDialog;
    private static final String TAG = "support";

    ActivitySupportBinding supportBinding;


    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed();
            return;
        }
        isBackPressed = true;
        Intent intent = new Intent(support.this, radio.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportBinding = ActivitySupportBinding.inflate(getLayoutInflater());
        setContentView(supportBinding.getRoot());
        allocateActivityTitle("Support");
        name = supportBinding.name;
        email = supportBinding.emailId;
        msg = supportBinding.query;
        sendbtn = supportBinding.Send;
        call = supportBinding.contact;
        email_us = supportBinding.mail;


        call.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+918960258960"));
            startActivity(intent);
        });


        email_us.setOnClickListener(view -> composeEmail());


        sendbtn.setOnClickListener(view -> {

            progressDialog = new ProgressDialog(support.this);

            boolean isValid = addUserdata();
            if(!isValid){
                return;
            }
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );

            sendbtn.setEnabled(false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxvgwIq2d-9mxCOzQVb55kajx09btw3Hln9UKufLaOQU5ikWsyCT7S6v4nPp3qnb6_7/exec", response -> {


                name.getText().clear();
                email.getText().clear();
                msg.getText().clear();
                Toast.makeText(support.this, "Thanks for writing a feedback", Toast.LENGTH_SHORT).show();
                sendbtn.setEnabled(true);
            }, error -> Toast.makeText(support.this, "Error in sending the feedback", Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();
                    params.put("action", "addUserdata");
                    params.put("name", name.getText().toString());
                    params.put("email", email.getText().toString());
                    params.put("msg", msg.getText().toString());
                    return params;

                }
            };

            RequestQueue queue = Volley.newRequestQueue(support.this);
            queue.add(stringRequest);


            if(name.getText().toString().isEmpty() && email.getText().toString().isEmpty() && msg.getText().toString().isEmpty()){
                Toast.makeText(support.this, "Please fill in all fields with valid values", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

            String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
            if(!email.getText().toString().matches(emailPattern)) {
                progressDialog.dismiss();
                return;
            }
            if(msg.getText().toString().isEmpty()){
                progressDialog.dismiss();
            }


            Runnable progressRunnable = () -> progressDialog.cancel();

            // ProgressBar timer to delay the progress

            Handler progCanceller = new Handler();
            progCanceller.postDelayed(progressRunnable, 3000);


        });

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

            Toast.makeText(support.this, "No supported application installed", Toast.LENGTH_LONG).show();
        }

    }




    private boolean addUserdata() {

        String UserName = name.getText().toString();
        String EmailId = email.getText().toString();
        String Query = msg.getText().toString();

        String PATTERN = "^[a-zA-Z\\d]{0,30}@[a-zA-Z\\d]{0,10}[.][a-zA-Z]{0,5}$";
        Pattern patt = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher match = patt.matcher(EmailId);


        if(UserName.isEmpty()){
            name.setError("Enter a valid name");
            name.requestFocus();
            return false;
        }else if(!match.matches()) {
            email.setError("Enter a valid email address");
            email.requestFocus();
            return false;
        } else if(Query.isEmpty()){
            msg.setError("Enter a valid query");
            msg.requestFocus();
            return false;
        }else{
            return true;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}