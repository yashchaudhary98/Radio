package com.example.radio_ksvcem;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class support extends AppCompatActivity {

    private boolean isbackPressed = false;
    EditText name, email, msg;
    TextView sendbtn, call, email_us;
    ProgressDialog progressDialog;
    private static final String TAG = "support";

    @Override
    public void onBackPressed() {
        if(isbackPressed) {
            super.onBackPressed();
            return;
        }
        isbackPressed = true;
        Intent intent = new Intent(support.this, radio.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        getSupportActionBar().hide();
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

                if(name.length() == 0) {
                    name.setError("Enter the Name");
                }else if(email.length() == 0) {
                    email.setError("Enter the Email");
                }else if(msg.length() == 0) {
                    msg.setError("Enter the Query");
                }else {
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );

                    addUserdata();
//
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        name.getText().clear();
                        email.getText().clear();
                        msg.getText().clear();
                        Toast.makeText(support.this, "Query has been uploaded", Toast.LENGTH_LONG).show();
                    }
                }, 5000);
            }
        });

    }

    public void composeEmail() {

        Log.d(TAG, "email_us");

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"Expertakv03@gmail.com", "Yashchaudharyx20@gmail.com"});

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