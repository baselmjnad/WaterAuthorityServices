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
        intent.putExtra("type","subscriptions");

        startActivity(intent);
    }
    public void GotoMyBills(View view){
        Intent ttt=getIntent();
        Intent intent=new Intent(UserActivity.this,MySubscriptions.class);
        intent.putExtra("userName",ttt.getStringExtra("userName") );
        intent.putExtra("userId",ttt.getStringExtra("userId"));
        intent.putExtra("type","bills");
        startActivity(intent);
    }
    public void GotoAttachSubscriptions(View view){
        Intent tt=getIntent();
        Intent intent=new Intent(UserActivity.this,AttachSubscriptions.class);
        intent.putExtra("userName",tt.getStringExtra("userName") );
        intent.putExtra("userId",tt.getStringExtra("userId"));
        intent.putExtra("type","attach");
        startActivity(intent);
    }
    public void GotoTransfer(View view){
        Intent ttt2=getIntent();
        Intent intent1=new Intent(UserActivity.this, Ownership.class);
        intent1.putExtra("userName",ttt2.getStringExtra("userName") );
        intent1.putExtra("userId",ttt2.getStringExtra("userId"));
        startActivity(intent1);
    }
    public void GotoClearanceRequest(View view){
        Intent ttt=getIntent();
        Intent intent=new Intent(UserActivity.this,MySubscriptions.class);
        intent.putExtra("userName",ttt.getStringExtra("userName") );
        intent.putExtra("userId",ttt.getStringExtra("userId"));
        intent.putExtra("type","clearance");
        startActivity(intent);

    }
    public void GotoRepair(View view){
        Intent ttt=getIntent();
        Intent intent=new Intent(UserActivity.this,MySubscriptions.class);
        intent.putExtra("userName",ttt.getStringExtra("userName") );
        intent.putExtra("userId",ttt.getStringExtra("userId"));
        intent.putExtra("type","repair");
        startActivity(intent);

    }
    public void GotoNewMeter(View view){
        Intent ttt=getIntent();
        Intent intent=new Intent(UserActivity.this,NewMeter.class);
        intent.putExtra("userName",ttt.getStringExtra("userName") );
        intent.putExtra("userId",ttt.getStringExtra("userId"));
        startActivity(intent);
    }
    public void GotoRequestsStatus(View view){
        Intent ttt=getIntent();
        Intent intent=new Intent(UserActivity.this,RequestsStatus.class);
        intent.putExtra("userName",ttt.getStringExtra("userName") );
        intent.putExtra("userId",ttt.getStringExtra("userId"));
        startActivity(intent);
    }
}