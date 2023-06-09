package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterauthorityservices.databinding.ActivityMySubscriptionsBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import Classes.Consumer;
import Classes.Department;
import Classes.Helper;
import Classes.ServicesRequest;
import Classes.Subscription;
import Classes.SubscriptionsListAdaptor;
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
    String userName, userId, conId;
    ArrayList<Subscription> list;
    Helper helper = new Helper();
    Consumer consumer1;

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
                    consumer1 = consumer;
                    MySubscriptions.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvMySubsConsumerName.setText(consumer.consumerName);
                            tvMySubsPhone.setText(consumer.consumerPhone);
                            tvMySubsAddress.setText(consumer.consumerAddress);
                            conId = consumer.id.toString();
                            GetSubs(conId);

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


    public void GetSubs(String id1) {
        tvMysubsError.setText("");
        OkHttpClient client1 = new OkHttpClient();
        Request request1 = new Request.Builder()
                .url(helper.MainUrl + "subscription/getconsumersubscriptions/" + id1)
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

                    Type type1 = new TypeToken<ArrayList<Subscription>>() {
                    }.getType();

                    ArrayList<Subscription> userArray = gson.fromJson(res, type1);

                    MySubscriptions.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list = userArray;
                            if (list.isEmpty() || list.size() == 0) {
                                tvMysubsError.setText("No Subscritptions for you!!");
                            } else {
                                Doit(list);
                            }

                        }
                    });


                } else {
                    tvMysubsError.setText("failed response");
                }
            }
        });


    }

    public void Doit(ArrayList<Subscription> listy) {
        SubscriptionsListAdaptor listAdaptor = new SubscriptionsListAdaptor(MySubscriptions.this, listy);
        binding.lvSubscriptions.setAdapter(listAdaptor);
        binding.lvSubscriptions.setClickable(true);
        binding.lvSubscriptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent t1 = getIntent();
                if (t1.getStringExtra("type").equals("subscriptions")) {
                    Toast.makeText(getApplicationContext(), listy.get(position).subscriptionStatus, Toast.LENGTH_SHORT).show();
                }
                if (t1.getStringExtra("type").equals("bills")) {
                    Intent intent = new Intent(MySubscriptions.this, MyInvoices.class);
                    intent.putExtra("barcode", listy.get(position).consumerBarCode);
                    startActivity(intent);
                }
                if (t1.getStringExtra("type").equals("status")) {
                    Intent intent = new Intent(MySubscriptions.this, RequestsStatus.class);
                    intent.putExtra("barcode", listy.get(position).consumerBarCode);
                    startActivity(intent);
                }
                if (t1.getStringExtra("type").equals("clearance")) {

                    ShowBoxOfClearance(listy.get(position));
                }
                if (t1.getStringExtra("type").equals("repair")) {

                    ShowBoxOfRepair(listy.get(position));
                }

            }
        });

    }

    private void ShowMesBox(String msg) {


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MySubscriptions.this);
        builder.setTitle("Success");
        builder.setMessage("Your request No is: " + msg);
        builder.setIcon(R.drawable.baseline_check_circle_24);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void ShowBoxOfClearance(Subscription subscription) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MySubscriptions.this);
        builder.setTitle("Confirmation!!");
        builder.setIcon(R.drawable.twotone_fmd_bad_24);
        builder.setMessage("Sure to send Clearance request for this subscription?");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SendClearanceRequest(subscription);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void SendClearanceRequest(Subscription subscription1) {
        ServicesRequest req = new ServicesRequest();
        Department department = new Department();
        department.departmentName = "ConsumersDep";
        department.id = 2;
        req.consumer = consumer1;
        req.subscription = subscription1;
        req.requestDate = Calendar.getInstance().getTime();
        req.requestType = "clearance";
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
                tvMysubsError.setText("Connection Error!");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {

                    String respString = response.body().string();
                    ServicesRequest req1 = gson.fromJson(respString, ServicesRequest.class);
                    MySubscriptions.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowMesBox(req1.id.toString());
                        }
                    });
                } else {
                    tvMysubsError.setText("Sending request Error!");
                }
            }
        });


    }

    public void ShowBoxOfRepair(Subscription subscription) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MySubscriptions.this);
        builder.setTitle("Confirmation!!");
        builder.setIcon(R.drawable.twotone_fmd_bad_24);
        builder.setMessage("This will send Repair request for this subscription Meter, continue?");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SendRepairRequest(subscription);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void SendRepairRequest(Subscription subscription1) {
        ServicesRequest req = new ServicesRequest();
        Department department = new Department();
        department.departmentName = "ConsumersDep";
        department.id = 2;
        req.consumer = consumer1;
        req.subscription = subscription1;
        req.requestDate = Calendar.getInstance().getTime();
        req.requestType = "repair";
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
                tvMysubsError.setText("Connection Error!");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {

                    String respString = response.body().string();
                    ServicesRequest req1 = gson.fromJson(respString, ServicesRequest.class);
                    MySubscriptions.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowMesBox(req1.id.toString());
                        }
                    });
                } else {
                    tvMysubsError.setText("Sending request Error!");
                }
            }
        });


    }


}