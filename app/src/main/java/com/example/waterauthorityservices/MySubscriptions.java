package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterauthorityservices.databinding.ActivityMySubscriptionsBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import Classes.Consumer;
import Classes.Helper;
import Classes.Subscription;
import Classes.SubscriptionsListAdaptor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MySubscriptions extends AppCompatActivity {
    ActivityMySubscriptionsBinding binding;
    TextView tvMySubsWelcome, tvMysubsError, tvMySubsConsumerName, tvMySubsPhone, tvMySubsAddress;
    String userName, userId,conId;
    ArrayList<Subscription> list;
    Helper helper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMySubscriptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();
        tvMySubsWelcome = findViewById(R.id.tvMySubsWelcome);
        tvMysubsError = findViewById(R.id.tvMysubsError);
        tvMySubsConsumerName = findViewById(R.id.tvMySubsConsumerName);
        tvMySubsPhone = findViewById(R.id.tvMySubsPhone);
        tvMySubsAddress = findViewById(R.id.tvMySubsAddress);
        Intent t = getIntent();
        userName = t.getStringExtra("userName");
        userId = t.getStringExtra("userId");
        tvMySubsWelcome.setText("User: " + userName.toUpperCase());
        //------------------------ShowConsumer(userId);---------------------------------------------
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(helper.MainUrl + "consumer/getbyuser/" + userId)
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
                            conId=consumer.id.toString();

                        }
                    });


                } else {
                    tvMysubsError.setText("System Error");
                }
            }
        });

        //-------------------------END SHOW consumer------------------------------------------------

        //----------GET Json array------------------------------------------------------------------

        //------------END Get Subscriptions---------------------------------------------------------
    }


    public void GetSubs(View view){
        tvMysubsError.setText("");
        OkHttpClient client1 = new OkHttpClient();
        Request request1 = new Request.Builder()
                .url(helper.MainUrl + "subscription/getconsumersubscriptions/" + conId)
                .build();
        client1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                tvMysubsError.setText("failed to connect to api");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response1) throws IOException {

                if (response1.isSuccessful()) {
                    String res = response1.body().string();
//                        JSONArray jsonArray = new JSONArray(res);
                    Gson gson = new Gson();

                    Type type1 = new TypeToken<ArrayList<Subscription>>(){}.getType();

                    ArrayList<Subscription> userArray = gson.fromJson(res, type1);

                    MySubscriptions.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list=userArray;
                            if(list.isEmpty()){
                                tvMysubsError.setText("No Subscritptions for you!!");
                            }

                        }
                    });


                } else {
                    tvMysubsError.setText("failed response");
                }
            }
        });

        SubscriptionsListAdaptor listAdaptor = new SubscriptionsListAdaptor(MySubscriptions.this, list);
        binding.lvSubscriptions.setAdapter(listAdaptor);
        binding.lvSubscriptions.setClickable(true);
        binding.lvSubscriptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), list.get(position).subscriptionStatus, Toast.LENGTH_SHORT).show();
            }
        });


    }

}