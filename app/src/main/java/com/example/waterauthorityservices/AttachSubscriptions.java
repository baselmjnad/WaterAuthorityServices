package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import Classes.Consumer;
import Classes.Subscription;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AttachSubscriptions extends AppCompatActivity {
    String userName, userId;
    TextView tvAttachWelcome,tvAttachError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_subscriptions);

        tvAttachWelcome = findViewById(R.id.tvAttachWelcome);
        tvAttachError = findViewById(R.id.tvAttachError);
        Intent t = getIntent();
        userName = t.getStringExtra("userName");
        userId = t.getStringExtra("userId");
        tvAttachWelcome.setText("Welcome " + userName.toUpperCase());

    }

    public void Test(View view) {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.wcs-api.somee.com/subscription/getWithConsumer/7")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                tvAttachError.setText("failed to connect to api");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Subscription subscription = gson.fromJson(res, Subscription.class);
                    AttachSubscriptions.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvAttachError.setText(subscription.consumer.consumerName);
                        }
                    });


                } else {
                    tvAttachError.setText("System Error");
                }
            }
        });

    }
}