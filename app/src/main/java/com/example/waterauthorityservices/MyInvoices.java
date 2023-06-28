package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterauthorityservices.databinding.ActivityMyinvoicesBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import Classes.Helper;
import Classes.Invoice;
import Classes.InvoicesListAdaptor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyInvoices extends AppCompatActivity {
    ActivityMyinvoicesBinding binding;

    TextView tvInvoicesBarcode,tvInvoicesTotal,tvError;
    String barCode;
    ArrayList<Invoice> list1;
    Helper helper = new Helper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyinvoicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list1 = new ArrayList<>();
        tvInvoicesBarcode=findViewById(R.id.tvInvoicesBarcode);
        tvError=findViewById(R.id.tvIncoiceError);
        tvInvoicesTotal=findViewById(R.id.tvInvoicesTotal);
        Intent t = getIntent();
        barCode = t.getStringExtra("barcode");
        tvInvoicesBarcode.setText(barCode);
        GetInvcoices();
    }
    public void GetInvcoices(){
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
                    ArrayList<Invoice> userArray1 = gson.fromJson(res, type1);
                    MyInvoices.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list1 = userArray1;
                            if (list1.size()== 0 || list1.isEmpty()) {
                                tvError.setText("No Invoices for this subscription!!");

                            }else {
                                list1 = userArray1;
                                Dodo(list1);

                            }
                        }
                    });
                } else {
                    tvError.setText("failed response");
                }
            }
        });

        }
        public void Dodo(ArrayList<Invoice> list2){
            Integer sum=0;
            for(Invoice invoice:list2){
                if(!invoice.invoiceStatus) {
                    sum += invoice.invoiceValue;
                }
            }
            tvInvoicesTotal.setText("not paid amount "+sum.toString()+" S.P");
            InvoicesListAdaptor listAdaptor1 = new InvoicesListAdaptor(MyInvoices.this, list2);
            binding.lvInvoices.setAdapter(listAdaptor1);
            binding.lvInvoices.setClickable(true);
            binding.lvInvoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), list2.get(position).invoiceValue.toString()+" S.P", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
