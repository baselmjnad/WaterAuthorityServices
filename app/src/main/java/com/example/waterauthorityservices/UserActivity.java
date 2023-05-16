package com.example.waterauthorityservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
    TextView tvMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tvMessage=findViewById(R.id.tvMessage);
        Intent intent=getIntent();
        tvMessage.setText("Welcome  "+intent.getStringExtra("userName"));
    }
}