package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import Classes.Consumer;
import Classes.Department;
import Classes.Helper;
import Classes.RequestDetails;
import Classes.SbyteDocument;
import Classes.ServicesRequest;
import Classes.Subscription;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Ownership extends AppCompatActivity {
    String userName, userId;
    EditText etTransferSearchBardode;
    Integer pic_id = 123;
    byte[] photArray;
    ImageView imCamera;

    LinearLayout lllSubs;
    TextView tvTransferWelcome, tvTransferError;
    TextView tvTransferConsumerName, tvTransferPhone, tvTransferAddress;
    TextView tvTransferSubsNumber, tvTransferSubsType, tvTransferSubsAddress;
    Helper helper = new Helper();
    Consumer consumer1 = new Consumer();
    Subscription subscription1 = new Subscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownership);


        tvTransferWelcome = findViewById(R.id.tvTransferWelcome);
        tvTransferError = findViewById(R.id.tvTransferError);
        tvTransferConsumerName = findViewById(R.id.tvTransferConsumerName);
        tvTransferPhone = findViewById(R.id.tvTransferPhone);
        tvTransferAddress = findViewById(R.id.tvTransferAddress);
        etTransferSearchBardode = findViewById(R.id.etTransferSearchBardode);
        lllSubs = findViewById(R.id.lllSubs);
        tvTransferSubsNumber = findViewById(R.id.tvTransferSubsNumber);
        tvTransferSubsType = findViewById(R.id.tvTransferSubsType);
        tvTransferSubsAddress = findViewById(R.id.tvTransferSubsAddress);
        imCamera = findViewById(R.id.imCamera);
        Intent t = getIntent();
        userName = t.getStringExtra("userName");
        userId = t.getStringExtra("userId");
        tvTransferWelcome.setText("User: " + userName.toUpperCase());
        lllSubs.setVisibility(View.INVISIBLE);
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
                tvTransferError.setText("failed to connect to api");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Consumer consumer = gson.fromJson(res, Consumer.class);

                    Ownership.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            consumer1 = consumer;
                            tvTransferConsumerName.setText(consumer.consumerName);
                            tvTransferPhone.setText(consumer.consumerPhone);
                            tvTransferAddress.setText(consumer.consumerAddress);
                        }
                    });
                } else {
                    tvTransferError.setText("System Error");
                }
            }
        });
    }

    public void SearchByBarcode(View view) {
        tvTransferError.setText("");
        lllSubs.setVisibility(View.INVISIBLE);
        if (etTransferSearchBardode.getText().toString().length() == 6) {
            Gson gson = new Gson();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(helper.MainUrl + "subscription/getbybarcode/" + etTransferSearchBardode.getText().toString().trim())
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    tvTransferError.setText("Connection Error!");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Subscription subscription = gson.fromJson(res, Subscription.class);
                        Ownership.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (subscription.subscriptionStatus.equals("active")) {
                                    if (subscription.consumer != null) {
                                        tvTransferSubsNumber.setText(subscription.consumerSubscriptionNo);
                                        tvTransferSubsType.setText(subscription.subscriptionUsingType);
                                        tvTransferSubsAddress.setText(subscription.subscriptionAddress);
                                        lllSubs.setVisibility(View.VISIBLE);
                                        subscription1 = subscription;
                                    } else {
                                        tvTransferError.setText("This subscription is not attached to a Consumer!!!");
                                    }
                                } else {
                                    tvTransferError.setText("this subscription is not active");
                                }
                            }
                        });


                    } else {
                        tvTransferError.setText("Subscription not found!");
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Barconde must be 6 Digits, example:004255", Toast.LENGTH_LONG).show();
        }
    }

    public void OpenCamera(View view) {
        photArray = null;
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, pic_id);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pic_id) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imCamera.setImageBitmap(photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            photArray = baos.toByteArray();
            try {
                baos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void SendRequest(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Ownership.this);
        builder.setTitle("Confirmation!!");
        builder.setIcon(R.drawable.twotone_fmd_bad_24);
        builder.setMessage("Are you sure you are requesting to transfer this subscription ownership to you?");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SendTransfer();
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

    private void SendTransfer() {
        if (photArray != null) {
            ServicesRequest req = new ServicesRequest();
            RequestDetails details = new RequestDetails();
            SbyteDocument sbyteDocument = new SbyteDocument();

            Department department = new Department();
            department.departmentName = "DocumentControllerDep";
            department.id = 1;
            req.consumer = consumer1;
            req.requestDate = Calendar.getInstance().getTime();
            req.requestType = "transfer";
            req.requestStatus = "onprogress";
            req.currentDepartment = department;
            req.details = details;
            sbyteDocument.request = req;
            sbyteDocument.document = photArray;

            OkHttpClient client = new OkHttpClient();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

            String json = gson.toJson(sbyteDocument);

            // request body start--------------------------------------------
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
            // request body end----------------------------------------------

            //request start----------------------------------------------------
            Request request = new Request.Builder()
                    .url(helper.MainUrl + "Request/postSbyte")
                    .post(requestBody)
                    .build();
            //request end----------------------------------------------------


            //response start----------------------------------------------------

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    tvTransferError.setText("Connection Error!");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    if (response.isSuccessful()) {

                        String respo = response.body().string();
                        Ownership.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowMesBox(respo);
                            }
                        });
                    } else {
                        tvTransferError.setText("Sending request Error!");
                    }
                }
            });
        } else {
            tvTransferError.setText("Please take photo for Apartment document");
        }

    }

    private void ShowMesBox(String reqNo) {


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Ownership.this);
        builder.setTitle("Success");
        builder.setMessage("Your Transfer ownership request No is: " + reqNo);
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

}

