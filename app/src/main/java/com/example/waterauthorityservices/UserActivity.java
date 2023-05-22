package com.example.waterauthorityservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
    TextView tvWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tvWelcome=findViewById(R.id.tvWelcome);
        Intent intent=getIntent();
        tvWelcome.setText("Welcome "+intent.getStringExtra("userName").toUpperCase());
    }
}