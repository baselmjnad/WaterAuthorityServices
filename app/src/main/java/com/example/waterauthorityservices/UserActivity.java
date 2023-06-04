package com.example.waterauthorityservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
    TextView tvWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tvWelcome=findViewById(R.id.tvWelcome);
        Intent t=getIntent();
        tvWelcome.setText("User: "+t.getStringExtra("userName").toUpperCase());
    }
    public void GotoMySubscriptions(View view){
        Intent tt=getIntent();
        Intent intent=new Intent(UserActivity.this,MySubscriptions.class);
        intent.putExtra("userName",tt.getStringExtra("userName") );
        intent.putExtra("userId",tt.getStringExtra("userId"));
        startActivity(intent);
    }
    public void GotoAttachSubscriptions(View view){
        Intent tt=getIntent();
        Intent intent=new Intent(UserActivity.this,AttachSubscriptions.class);
        intent.putExtra("userName",tt.getStringExtra("userName") );
        intent.putExtra("userId",tt.getStringExtra("userId"));
        startActivity(intent);
    }
}