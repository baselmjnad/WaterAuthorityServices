package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterauthorityservices.databinding.ActivityMySubscriptionsBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import Classes.Consumer;
import Classes.Helper;
import Classes.Subscription;
import Classes.SubscriptionsListAdaptor;
import Classes.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MySubscriptions extends AppCompatActivity {
    ActivityMySubscriptionsBinding binding;
    TextView tvMySubsWelcome, tvMysubsError, tvMySubsConsumerName, tvMySubsPhone, tvMySubsAddress;
    String userName, userId;
    Helper helper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMySubscriptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        //----------temp list-------------------
        Subscription subscription1=new Subscription();
        subscription1.id=1;
        subscription1.consumerSubscriptionNo="111111";
        subscription1.subscriptionAddress="Dubai";
        subscription1.consumerBarCode="111000";
        subscription1.subscriptionUsingType="Home";
        subscription1.subscriptionStatus="active";
        Subscription subscription2=new Subscription();
        subscription2.id=2;
        subscription2.consumerSubscriptionNo="222222";
        subscription2.subscriptionUsingType="Industrial";
        subscription2.subscriptionAddress="Dubai";
        subscription2.consumerBarCode="222000";
        subscription2.subscriptionStatus="inactive";
        ArrayList<Subscription> list=new ArrayList<>();
        list.add(subscription1);
        list.add(subscription2);

        //----------end temp list---------------
        SubscriptionsListAdaptor listAdaptor=new SubscriptionsListAdaptor(MySubscriptions.this,list);
        binding.lvSubscriptions.setAdapter(listAdaptor);
        binding.lvSubscriptions.setClickable(true);
        binding.lvSubscriptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), list.get(position).consumerBarCode, Toast.LENGTH_LONG).show();
            }
        });
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