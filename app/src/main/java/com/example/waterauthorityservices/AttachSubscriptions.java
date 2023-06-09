package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Classes.Consumer;
import Classes.Department;
import Classes.Helper;
import Classes.ServicesRequest;
import Classes.Subscription;
import Classes.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AttachSubscriptions extends AppCompatActivity {
    String userName, userId;
    EditText etSearchBarcode;
    LinearLayout llSubs;
    TextView tvAttachWelcome, tvAttachError;
    TextView tvMyAttachConsumerName, tvAttachPhone, tvAttachAddress;
    TextView tvSubsNumber, tvSubsType, tvSubsAddress;
    Helper helper = new Helper();
    Consumer consumer1 = new Consumer();
    Subscription subscription1 = new Subscription();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_subscriptions);

        tvAttachWelcome = findViewById(R.id.tvAttachWelcome);
        tvAttachError = findViewById(R.id.tvAttachError);
        tvMyAttachConsumerName = findViewById(R.id.tvMyAttachConsumerName);
        tvAttachPhone = findViewById(R.id.tvAttachPhone);
        tvAttachAddress = findViewById(R.id.tvAttachAddress);
        etSearchBarcode = findViewById(R.id.etSearchBardode);
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
                            consumer1 = consumer;
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
        if (etSearchBarcode.getText().toString().length() == 6) {
            Gson gson = new Gson();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(helper.MainUrl + "subscription/getbybarcode/" + etSearchBarcode.getText().toString().trim())
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
                                    if (subscription.consumer == null) {
                                        tvSubsNumber.setText(subscription.consumerSubscriptionNo);
                                        tvSubsType.setText(subscription.subscriptionUsingType);
                                        tvSubsAddress.setText(subscription.subscriptionAddress);
                                        llSubs.setVisibility(View.VISIBLE);
                                        subscription1 = subscription;
                                    } else {
                                        tvAttachError.setText("This subscription is attached to a Consumer!!!");
                                    }
                                } else {
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

    public void Send() {
        ServicesRequest req = new ServicesRequest();
        Department department = new Department();
        department.departmentName = "ConsumersDep";
        department.id = 2;
        req.consumer = consumer1;
        req.subscription = subscription1;
        req.requestDate = Calendar.getInstance().getTime();
        req.requestType = "attach";
        req.requestStatus = "onprogress";
        req.currentDepartment = department;

        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        String json = gson.toJson(req);

        // request body start--------------------------------------------
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        // request body end----------------------------------------------

        //request start----------------------------------------------------
        Request request = new Request.Builder()
                .url(helper.MainUrl + "Request")
                .post(requestBody)
                .build();
        //request end----------------------------------------------------


        //response start----------------------------------------------------

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                tvAttachError.setText("Connection Error!");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {

                    String respString = response.body().string();
                    ServicesRequest req1 = gson.fromJson(respString, ServicesRequest.class);
                    AttachSubscriptions.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "YOUR REQUEST IS SENT", Toast.LENGTH_LONG).show();
                            tvAttachError.setText("YOUR REQUEST No : " + req1.id.toString());
                            llSubs.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    tvAttachError.setText("Sending request Error!");
                }
            }
        });


    }

    public void SendRequest(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AttachSubscriptions.this);
        builder.setTitle("Confirmation!!");
        builder.setIcon(R.drawable.twotone_fmd_bad_24);
        builder.setMessage("Are you sure this subscription belongs to you?");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Send();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();           }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}