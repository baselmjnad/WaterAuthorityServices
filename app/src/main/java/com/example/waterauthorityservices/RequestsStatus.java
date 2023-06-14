package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import Classes.Helper;
import Classes.Invoice;
import Classes.InvoicesListAdaptor;
import Classes.RequstsListAdaptor;
import Classes.ServicesRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestsStatus extends AppCompatActivity {
    ActivityRequestsStatusBinding binding;
    TextView tvRequestBarcode, tvRequestError;
    String barCode;
    ArrayList<ServicesRequest> list1;
    Helper helper = new Helper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestsStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list1 = new ArrayList<>();
        tvRequestBarcode = findViewById(R.id.tvRequestBarcode);
        tvRequestError = findViewById(R.id.tvRequestError);
        Intent t = getIntent();
        barCode = t.getStringExtra("barcode");
        tvRequestBarcode.setText(barCode);
        GetRequests();

    }

    private void GetRequests() {
        tvRequestError.setText("");
        OkHttpClient client1 = new OkHttpClient();
        Request request1 = new Request.Builder()
                .url(helper.MainUrl + "request/getRequestsByBarcode/" + barCode)
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
                    Type type1 = new TypeToken<ArrayList<ServicesRequest>>() {
                    }.getType();
                    ArrayList<ServicesRequest> userArray1 = gson.fromJson(res, type1);
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

    private void Dodo(ArrayList<ServicesRequest> list1) {
        RequstsListAdaptor listAdaptor1 = new RequstsListAdaptor(RequestsStatus.this, list1);

        binding.lvRequests.setAdapter(listAdaptor1);
        binding.lvRequests.setClickable(true);
        binding.lvRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), list1.get(position).requestStatus, Toast.LENGTH_SHORT).show();
            }
        });

    }

}

