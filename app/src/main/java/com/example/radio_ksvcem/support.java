package com.example.radio_ksvcem;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
        name = findViewById(R.id.name);
        email = findViewById(R.id.email_id);
        msg = findViewById(R.id.query);
        sendbtn = findViewById(R.id.Send);
        call = findViewById(R.id.contact);
        email_us = findViewById(R.id.mail);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:11111111"));
                startActivity(intent);
            }
        });


        email_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail();
            }
        });


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(support.this);


                addUserdata();
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                if(name.length() == 0 && email.length() == 0 && msg.length() == 0){
                    progressDialog.dismiss();
                }

                Runnable progressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                    }
                };

                Handler progCanceller = new Handler();
                progCanceller.postDelayed(progressRunnable, 3000);


                name.getText().clear();
                email.getText().clear();
                msg.getText().clear();
            }

        });

    }

    public void composeEmail() {

        Log.d(TAG, "email_us");

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"Expertakv03@gmail.com", "Yashchaudharyx20@gmail.com"});

        try {

            startActivity(Intent.createChooser(intent, "choose any below application"));
        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(support.this, "No supported application installed", Toast.LENGTH_LONG).show();
        }

    }




    private void addUserdata() {

        String UserName = name.getText().toString();
        String EmailId = email.getText().toString();
        String Query = msg.getText().toString();

        String PATTERN = "^[a-zA-Z0-9]{0,30}[@][a-zA-Z0-9]{0,10}[.][a-zA-Z]{0,5}$";
        Pattern patt = Pattern.compile(PATTERN);
        Matcher match = patt.matcher(email.getText());


        if(UserName.isEmpty()){
            name.setError("Enter the name");
            name.requestFocus();
            return;
        }

        if(!match.matches()) {
            email.setError("Enter the email");
            email.requestFocus();
            return;
        }

        if(Query.isEmpty()){
            msg.setError("Enter the query");
            msg.requestFocus();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxvgwIq2d-9mxCOzQVb55kajx09btw3Hln9UKufLaOQU5ikWsyCT7S6v4nPp3qnb6_7/exec", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("action", "addUserdata");
                params.put("name", UserName);
                params.put("email", EmailId);
                params.put("msg", Query);
                return params;

            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }


}