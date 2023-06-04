package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import Classes.Consumer;
import Classes.Helper;
import Classes.Subscription;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AttachSubscriptions extends AppCompatActivity {
    String userName, userId;
    EditText etSearchBardode;
    LinearLayout llSubs;
    TextView tvAttachWelcome, tvAttachError;
    TextView tvMyAttachConsumerName, tvAttachPhone, tvAttachAddress;
    TextView tvSubsNumber, tvSubsType, tvSubsAddress;
    Helper helper = new Helper();
    Consumer consumer1 = new Consumer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_subscriptions);

        tvAttachWelcome = findViewById(R.id.tvAttachWelcome);
        tvAttachError = findViewById(R.id.tvAttachError);
        tvMyAttachConsumerName = findViewById(R.id.tvMyAttachConsumerName);
        tvAttachPhone = findViewById(R.id.tvAttachPhone);
        tvAttachAddress = findViewById(R.id.tvAttachAddress);
        etSearchBardode = findViewById(R.id.etSearchBardode);
        llSubs = findViewById(R.id.llSubs);
        tvSubsNumber = findViewById(R.id.tvSubsNumber);
        tvSubsType = findViewById(R.id.tvSubsType);
        tvSubsAddress = findViewById(R.id.tvSubsAddress);
        Intent t = getIntent();
        userName = t.getStringExtra("userName");
        userId = t.getStringExtra("userId");
        tvAttachWelcome.setText("User: " + userName.toUpperCase());
        llSubs.setVisibility(View.INVISIBLE);
        GetConsumer(userId);

    }

    public void GetConsumer(String id) {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(helper.MainUrl + "consumer/getbyuser/" + id)
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
                    Consumer consumer = gson.fromJson(res, Consumer.class);
                    AttachSubscriptions.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvMyAttachConsumerName.setText(consumer.consumerName);
                            tvAttachPhone.setText(consumer.consumerPhone);
                            tvAttachAddress.setText(consumer.consumerAddress);
                        }
                    });


                } else {
                    tvAttachError.setText("System Error");
                }
            }
        });


    }

    public void SearchByBarcode(View view) {
        tvAttachError.setText("");
        llSubs.setVisibility(View.INVISIBLE);
        if (etSearchBardode.getText().toString().length() == 6) {
            Gson gson = new Gson();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(helper.MainUrl + "subscription/getbybarcode/" + etSearchBardode.getText().toString().trim())
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    tvAttachError.setText("Connection Error!");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Subscription subscription = gson.fromJson(res, Subscription.class);
                        AttachSubscriptions.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (subscription.subscriptionStatus.equals("active")) {
                                    if(subscription.consumer==null){
                                        tvSubsNumber.setText(subscription.consumerSubscriptionNo);
                                        tvSubsType.setText(subscription.subscriptionUsingType);
                                        tvSubsAddress.setText(subscription.subscriptionAddress);
                                        llSubs.setVisibility(View.VISIBLE);
                                    }else {
                                        tvAttachError.setText("This subscription is attached to another Consumer");
                                    }
                                }else {
                                    tvAttachError.setText("this subscription is not active");
                                }
                            }
                        });


                    } else {
                        tvAttachError.setText("Subscription not found!");
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Barconde must be 6 Digits, example:004255", Toast.LENGTH_LONG).show();
        }

    }

}