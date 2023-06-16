package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterauthorityservices.databinding.ActivityMyinvoicesBinding;
import com.example.waterauthorityservices.databinding.ActivityRequestsStatusBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import Classes.Consumer;
import Classes.Helper;
import Classes.Invoice;
import Classes.InvoicesListAdaptor;
import Classes.RequestVM;
import Classes.RequstsListAdaptor;
import Classes.ServicesRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestsStatus extends AppCompatActivity {
    ActivityRequestsStatusBinding binding;
    TextView tvRequestError, tvMyReqConsumerName, tvMyReqPhone, tvMyReqAddress;
    ArrayList<RequestVM> list1;
    Helper helper = new Helper();
    String userName, userId, conId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestsStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list1 = new ArrayList<>();
        tvRequestError = findViewById(R.id.tvRequestError);
        tvMyReqConsumerName = findViewById(R.id.tvMyReqConsumerName);
        tvMyReqPhone = findViewById(R.id.tvMyReqPhone);
        tvMyReqAddress = findViewById(R.id.tvMyReqAddress);
        Intent t = getIntent();
        userName = t.getStringExtra("userName");
        userId = t.getStringExtra("userId");

        GetCounsumer();

    }

    public void GetCounsumer() {
        //------------------------ShowConsumer(userId);---------------------------------------------
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(helper.MainUrl + "consumer/getbyuser/" + userId)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                tvRequestError.setText("failed to connect to api");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Consumer consumer = gson.fromJson(res, Consumer.class);
                    RequestsStatus.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvMyReqConsumerName.setText(consumer.consumerName);
                            tvMyReqPhone.setText(consumer.consumerPhone);
                            tvMyReqAddress.setText(consumer.consumerAddress);
                            conId = consumer.id.toString();
                            GetRequests(conId);
                        }
                    });


                } else {
                    tvRequestError.setText("System Error");
                }
            }
        });

    }

    private void GetRequests(String cid) {
        tvRequestError.setText("");
        OkHttpClient client1 = new OkHttpClient();
        Request request1 = new Request.Builder()
                .url(helper.MainUrl + "request/getByConsumer/"+ cid)
                .build();
        client1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                tvRequestError.setText("failed to connect to api");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response1) throws IOException {

                if (response1.isSuccessful()) {
                    String res = response1.body().string();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    Type type1 = new TypeToken<ArrayList<RequestVM>>() {
                    }.getType();
                    ArrayList<RequestVM> userArray1 = gson.fromJson(res, type1);
                    RequestsStatus.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list1 = userArray1;
                            if (list1.size() == 0 || list1.isEmpty()) {
                                tvRequestError.setText("No Requests for this subscription!!");
                            } else {
                                list1 = userArray1;
                                Dodo(list1);

                            }
                        }
                    });
                } else {
                    tvRequestError.setText("failed response");
                }
            }
        });

    }

    private void Dodo(ArrayList<RequestVM> list1) {
        RequstsListAdaptor listAdaptor1 = new RequstsListAdaptor(RequestsStatus.this, list1);

        binding.lvRequests.setAdapter(listAdaptor1);
        binding.lvRequests.setClickable(true);
        binding.lvRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), list1.get(position).request.requestType, Toast.LENGTH_SHORT).show();
                ShowMesBox(list1.get(position));
            }
        });

    }
    private void ShowMesBox(RequestVM requestVM) {


        AlertDialog.Builder builder = new AlertDialog.Builder(RequestsStatus.this);
        builder.setTitle("Details");
        String nl=" \n";
        String sss="";
        sss+="-Request No: "+requestVM.request.id.toString() + nl;
        sss+="-Request Type: "+requestVM.request.requestType + nl;
        sss+="-Request Status: "+requestVM.request.requestStatus + nl;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(requestVM.request.requestDate);
        String ss=String.valueOf(calendar.get(calendar.DAY_OF_MONTH))+"-"+String.valueOf(calendar.get(calendar.MONTH))+"-"+String.valueOf(calendar.get(calendar.YEAR ));
        sss+="-Request Date: "+ss + nl;
        if(requestVM.request.subscription!=null){
            sss+="-Request Subscription Barcode: "+requestVM.request.subscription.consumerBarCode + nl;
            sss+="-Request Subscription type: "+requestVM.request.subscription.subscriptionUsingType + nl;
            sss+="-Request Subscription Status: "+requestVM.request.subscription.subscriptionStatus + nl;
        }
        if(requestVM.request.requestStatus.equals("completed")){
            Calendar calendar2 = Calendar.getInstance();
            calendar.setTime(requestVM.finishDate);
            String ss2=String.valueOf(calendar.get(calendar.DAY_OF_MONTH))+"-"+String.valueOf(calendar.get(calendar.MONTH))+"-"+String.valueOf(calendar.get(calendar.YEAR ));

            sss+="-Request completion Date : "+ss2 + nl;
            }
        if(requestVM.request.requestStatus.equals("rejected")){
            Calendar calendar1 = Calendar.getInstance();
            calendar.setTime(requestVM.request.requestDate);
            String ss1=String.valueOf(calendar.get(calendar.DAY_OF_MONTH))+"-"+String.valueOf(calendar.get(calendar.MONTH))+"-"+String.valueOf(calendar.get(calendar.YEAR ));


            sss+="-Request rejection Date : "+ss1 + nl;
            sss+="-Request rejection Department : "+requestVM.rejectedBy.departmentName + nl;
            sss+="-Request rejection Details : "+requestVM.rejectNotes + nl;
            }
        builder.setMessage(sss);
        builder.setIcon(R.drawable.baseline_comment_24);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}

