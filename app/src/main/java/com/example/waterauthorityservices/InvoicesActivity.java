package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterauthorityservices.databinding.ActivityInvoicesBinding;
import com.example.waterauthorityservices.databinding.ActivityMySubscriptionsBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import Classes.Helper;
import Classes.Invoice;
import Classes.InvoicesListAdaptor;
import Classes.Subscription;
import Classes.SubscriptionsListAdaptor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InvoicesActivity extends AppCompatActivity {
    ActivityInvoicesBinding binding;

    TextView tvInvoicesBarcode,tvInvoicesTotal,tvError;
    String barCode="";
    ArrayList<Invoice> list;
    Helper helper = new Helper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvoicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();
        tvInvoicesBarcode=findViewById(R.id.tvInvoicesBarcode);
        tvError=findViewById(R.id.tvIncoiceError);
        tvInvoicesTotal=findViewById(R.id.tvInvoicesTotal);
        Intent t = getIntent();
        barCode = t.getStringExtra("barcode");
    }
    public void GetIncoices(View view){
        tvError.setText("");
        OkHttpClient client1 = new OkHttpClient();
        Request request1 = new Request.Builder()
                .url(helper.MainUrl + "invoice/getByBarcode/" + barCode)
                .build();
        client1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                tvError.setText("failed to connect to api");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response1) throws IOException {

                if (response1.isSuccessful()) {
                    String res = response1.body().string();
                    Gson gson = new Gson();
                    Type type1 = new TypeToken<ArrayList<Invoice>>() {
                    }.getType();
                    ArrayList<Invoice> userArray = gson.fromJson(res, type1);

                    InvoicesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list = userArray;
                            if (list.size()==0 || list.isEmpty()) {
                                tvError.setText("No Invoices for this subscription!!");
                            }
                        }
                    });
                } else {
                    tvError.setText("failed response");
                }
            }
        });

        InvoicesListAdaptor listAdaptor = new InvoicesListAdaptor(InvoicesActivity.this, list);

        binding.lvInvoices.setAdapter(listAdaptor);
        binding.lvInvoices.setClickable(true);
        binding.lvInvoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), list.get(position).invoiceValue, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
