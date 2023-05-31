package com.example.waterauthorityservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MySubscriptions extends AppCompatActivity {
    TextView tvMySubscrWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscriptions);
        tvMySubscrWelcome=findViewById(R.id.tvMySubsWelcome);
        Intent t=getIntent();
        tvMySubscrWelcome.setText("Welcome "+t.getStringExtra("userName").toUpperCase());

    }

}