package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import Classes.Consumer;
import Classes.Helper;
import Classes.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MySubscriptions extends AppCompatActivity {

    TextView tvMySubsWelcome, tvMysubsError, tvMySubsConsumerName, tvMySubsPhone, tvMySubsAddress;
    String userName, userId;
    Helper helper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscriptions);

        tvMySubsWelcome = findViewById(R.id.tvMySubsWelcome);
        tvMysubsError = findViewById(R.id.tvMysubsError);
        tvMySubsConsumerName = findViewById(R.id.tvMySubsConsumerName);
        tvMySubsPhone = findViewById(R.id.tvMySubsPhone);
        tvMySubsAddress = findViewById(R.id.tvMySubsAddress);
        Intent t = getIntent();
        userName = t.getStringExtra("userName");
        userId = t.getStringExtra("userId");
        tvMySubsWelcome.setText("User: " + userName.toUpperCase());
        ShowConsumer(userId);

    }

    public void ShowConsumer(String id) {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(helper.MainUrl + "consumer/getbyuser/"+id)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                tvMysubsError.setText("failed to connect to api");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Consumer consumer = gson.fromJson(res, Consumer.class);
                    MySubscriptions.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvMySubsConsumerName.setText(consumer.consumerName);
                            tvMySubsPhone.setText(consumer.consumerPhone);
                            tvMySubsAddress.setText(consumer.consumerAddress);
                        }
                    });


                } else {
                    tvMysubsError.setText("System Error");
                }
            }
        });


    }
}